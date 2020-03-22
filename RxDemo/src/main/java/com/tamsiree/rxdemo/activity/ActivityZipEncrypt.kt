package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.orhanobut.logger.Logger
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxkit.RxFileTool
import com.tamsiree.rxkit.RxZipTool
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import java.io.File
import java.io.IOException

/**
 * @author tamsiree
 */
class ActivityZipEncrypt : ActivityBase() {
    @JvmField
    @BindView(R.id.btn_create_folder)
    var mBtnCreateFolder: Button? = null

    @JvmField
    @BindView(R.id.btn_zip)
    var mBtnZip: Button? = null

    @JvmField
    @BindView(R.id.tv_state)
    var mTvState: TextView? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.btn_upzip)
    var mBtnUpzip: Button? = null

    @JvmField
    @BindView(R.id.btn_zip_delete_dir)
    var mBtnZipDeleteDir: Button? = null

    @JvmField
    @BindView(R.id.Progress)
    var mProgress: ProgressBar? = null
    private var fileDir: File? = null
    private var fileTempDir: File? = null
    private var unZipDirFile: File? = null
    private var fileZip: File? = null
    private var zipPath: String? = null
    private var zipParentPath: String? = null
    private var zipTempDeletePath: String? = null
    private var unzipPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zip_encrypt)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)

    }

    override fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        zipParentPath = RxFileTool.rootPath?.absolutePath + File.separator + "RxTool"
        zipTempDeletePath = RxFileTool.rootPath?.absolutePath + File.separator + "RxTool" + File.separator + "RxTempTool"
        unzipPath = RxFileTool.rootPath?.absolutePath + File.separator + "解压缩文件夹"
        zipPath = RxFileTool.rootPath?.absolutePath + File.separator + "Rxtool.zip"
        unZipDirFile = File(unzipPath)
        if (!unZipDirFile!!.exists()) {
            unZipDirFile!!.mkdirs()
        }
    }

    override fun initData() {

    }

    @OnClick(R.id.btn_create_folder, R.id.btn_zip, R.id.btn_upzip, R.id.btn_zip_delete_dir)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_create_folder -> {
                fileDir = File(zipParentPath)
                fileTempDir = File(zipTempDeletePath)
                if (!fileDir!!.exists()) {
                    fileDir!!.mkdirs()
                }
                if (!fileTempDir!!.exists()) {
                    fileTempDir!!.mkdirs()
                }
                try {
                    val file = File.createTempFile("被压缩文件ŐεŐ", ".txt", fileDir)
                    val file1 = File.createTempFile("待删除文件o(╥﹏╥)o", ".txt", fileTempDir)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                mTvState!!.text = "临时文件 创建成功,文件位于根目录的RxTool里(✺ω✺)"
            }
            R.id.btn_zip -> {
                fileZip = File(zipPath)
                if (fileZip!!.exists()) {
                    RxFileTool.deleteFile(fileZip)
                    Logger.d("导出文件已存在，将之删除")
                }
                if (fileDir != null) {
                    if (fileDir!!.exists()) {
                        val result = RxZipTool.zipEncrypt(fileDir!!.absolutePath, fileZip!!.absolutePath, true, "123456")
                        mTvState!!.text = "压缩并加密成功,路径$result"
                    } else {
                        RxToast.error("导出的文件不存在")
                    }
                } else {
                    RxToast.error("导出的文件不存在")
                }
            }
            R.id.btn_upzip -> {
                val zipFiles = RxZipTool.unzipFileByKeyword(fileZip, unZipDirFile, "123456")
                var str = "导出文件列表(*▽*)\n"
                if (zipFiles != null) {
                    for (zipFile in zipFiles) {
                        str += """
                            ${zipFile.absolutePath}


                            """.trimIndent()
                    }
                }
                mTvState!!.text = str
            }
            R.id.btn_zip_delete_dir -> if (RxZipTool.removeDirFromZipArchive(zipPath, "RxTool" + File.separator + "RxTempTool")) {
                mTvState!!.text = "RxTempTool 删除成功"
            } else {
                mTvState!!.text = "RxTempTool 删除失败"
            }
            else -> {
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val _handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RxZipTool.CompressStatus.START -> {
                    mTvState!!.text = "Start..."
                    mProgress!!.visibility = View.VISIBLE
                }
                RxZipTool.CompressStatus.HANDLING -> {
                    val bundle = msg.data
                    val percent = bundle.getInt(RxZipTool.CompressKeys.PERCENT)
                    mTvState!!.text = "$percent%"
                    mProgress!!.progress = percent
                }
                RxZipTool.CompressStatus.ERROR -> {
                    val bundle = msg.data
                    val error = bundle.getString(RxZipTool.CompressKeys.ERROR)
                    mTvState!!.text = error
                }
                RxZipTool.CompressStatus.COMPLETED -> {
                    mTvState!!.text = "Completed"
                    mProgress!!.visibility = View.INVISIBLE
                }
                else -> {
                }
            }
        }
    }
}