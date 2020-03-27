package com.tamsiree.rxkit.photomagic

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxkit.photomagic.Checker.checkSuffix
import com.tamsiree.rxkit.photomagic.Checker.isImage
import com.tamsiree.rxkit.photomagic.Checker.isNeedCompress
import java.io.File
import java.io.IOException
import java.util.*

class RxMagic private constructor(builder: Builder) : Handler.Callback {
    private var mTargetDir: String?
    private val mPaths: MutableList<String>?
    private val mLeastCompressSize: Int
    private val mCompressListener: OnCompressListener?
    private val mHandler: Handler

    /**
     * Returns a mFile with a cache audio name in the private cache directory.
     *
     * @param context A context.
     */
    private fun getImageCacheFile(context: Context, suffix: String): File {
        if (TextUtils.isEmpty(mTargetDir)) {
            mTargetDir = getImageCacheDir(context)!!.absolutePath
        }
        val cacheBuilder = mTargetDir + "/" +
                System.currentTimeMillis() +
                (Math.random() * 1000).toInt() +
                if (TextUtils.isEmpty(suffix)) ".jpg" else suffix
        return File(cacheBuilder)
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store retrieved audio.
     *
     * @param context A context.
     * @see .getImageCacheDir
     */
    private fun getImageCacheDir(context: Context): File? {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR)
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see .getImageCacheDir
     */
    private fun getImageCacheDir(context: Context, cacheName: String): File? {
        val cacheDir = context.externalCacheDir
        if (cacheDir != null) {
            val result = File(cacheDir, cacheName)
            return if (!result.mkdirs() && (!result.exists() || !result.isDirectory)) {
                // File wasn't able to create a directory, or the result exists but not a directory
                null
            } else result
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            TLog.e(TAG, "default disk cache dir is null")
        }
        return null
    }

    /**
     * start asynchronous compress thread
     */
    @UiThread
    private fun launch(context: Context) {
        if (mPaths == null || mPaths.size == 0 && mCompressListener != null) {
            mCompressListener!!.onError(NullPointerException("image file cannot be null"))
        }
        val iterator = mPaths!!.iterator()
        while (iterator.hasNext()) {
            val path = iterator.next()
            if (isImage(path)) {
                AsyncTask.SERIAL_EXECUTOR.execute {
                    try {
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START))
                        val result = if (isNeedCompress(mLeastCompressSize, path)) Engine(path, getImageCacheFile(context, checkSuffix(path))).compress() else File(path)
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, result))
                    } catch (e: IOException) {
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e))
                    }
                }
            } else {
                mCompressListener!!.onError(IllegalArgumentException("can not read the path : $path"))
            }
            iterator.remove()
        }
    }

    /**
     * start compress and return the mFile
     */
    @WorkerThread
    @Throws(IOException::class)
    private operator fun get(path: String, context: Context): File {
        return Engine(path, getImageCacheFile(context, checkSuffix(path))).compress()
    }

    @WorkerThread
    @Throws(IOException::class)
    private operator fun get(context: Context): List<File> {
        val results: MutableList<File> = ArrayList()
        val iterator = mPaths!!.iterator()
        while (iterator.hasNext()) {
            val path = iterator.next()
            if (isImage(path)) {
                results.add(Engine(path, getImageCacheFile(context, checkSuffix(path))).compress())
            }
            iterator.remove()
        }
        return results
    }

    override fun handleMessage(msg: Message): Boolean {
        if (mCompressListener == null) {
            return false
        }
        when (msg.what) {
            MSG_COMPRESS_START -> mCompressListener.onStart()
            MSG_COMPRESS_SUCCESS -> mCompressListener.onSuccess(msg.obj as File)
            MSG_COMPRESS_ERROR -> mCompressListener.onError(msg.obj as Throwable)
            else -> {
            }
        }
        return false
    }

    class Builder internal constructor(private val context: Context) {
        internal var mTargetDir: String? = null
        internal val mPaths: MutableList<String>
        internal var mLeastCompressSize = 100
        internal var mCompressListener: OnCompressListener? = null
        private fun build(): RxMagic {
            return RxMagic(this)
        }

        fun load(file: File): Builder {
            mPaths.add(file.absolutePath)
            return this
        }

        fun load(string: String): Builder {
            mPaths.add(string)
            return this
        }

        fun load(list: List<String>?): Builder {
            mPaths.addAll(list!!)
            return this
        }

        fun putGear(gear: Int): Builder {
            return this
        }

        fun setCompressListener(listener: OnCompressListener?): Builder {
            mCompressListener = listener
            return this
        }

        fun setTargetDir(targetDir: String?): Builder {
            mTargetDir = targetDir
            return this
        }

        /**
         * do not compress when the origin image file size less than one value
         *
         * @param size the value of file size, unit KB, default 100K
         */
        fun ignoreBy(size: Int): Builder {
            mLeastCompressSize = size
            return this
        }

        /**
         * begin compress image with asynchronous
         */
        fun launch() {
            build().launch(context)
        }

        @Throws(IOException::class)
        operator fun get(path: String): File {
            return build()[path, context]
        }

        /**
         * begin compress image with synchronize
         *
         * @return the thumb image file list
         */
        @Throws(IOException::class)
        fun get(): List<File> {
            return build()[context]
        }

        init {
            mPaths = ArrayList()
        }
    }

    companion object {
        private const val TAG = "RxMagic"
        private const val DEFAULT_DISK_CACHE_DIR = "rxmagic_disk_cache"
        private const val MSG_COMPRESS_SUCCESS = 0
        private const val MSG_COMPRESS_START = 1
        private const val MSG_COMPRESS_ERROR = 2

        @JvmStatic
        fun createBuilder(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        mPaths = builder.mPaths
        mTargetDir = builder.mTargetDir
        mCompressListener = builder.mCompressListener
        mLeastCompressSize = builder.mLeastCompressSize
        mHandler = Handler(Looper.getMainLooper(), this)
    }
}