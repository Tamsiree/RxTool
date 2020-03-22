package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.orhanobut.logger.Logger
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxkit.RxFileTool
import com.tamsiree.rxkit.RxZipTool
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_zip_encrypt.*
import java.io.File
import java.io.IOException

/**
 * @author tamsiree
 */
class ActivityZipEncrypt : ActivityBase() {

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
        RxDeviceTool.setPortrait(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        zipParentPath = RxFileTool.rootPath?.absolutePath + File.separator + "RxTool"
        zipTempDeletePath = RxFileTool.rootPath?.absolutePath + File.separator + "RxTool" + File.separator + "RxTempTool"
        unzipPath = RxFileTool.rootPath?.absolutePath + File.separator + "解压缩文件夹"
        zipPath = RxFileTool.rootPath?.absolutePath + File.separator + "Rxtool.zip"
        unZipDirFile = File(unzipPath)
        if (!unZipDirFile!!.exists()) {
            unZipDirFile!!.mkdirs()
        }

        btn_create_folder.setOnClickListener {
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
            tv_state.text = "临时文件 创建成功,文件位于根目录的RxTool里(✺ω✺)"
        }
        btn_zip.setOnClickListener {
            fileZip = File(zipPath)
            if (fileZip!!.exists()) {
                RxFileTool.deleteFile(fileZip)
                Logger.d("导出文件已存在，将之删除")
            }
            if (fileDir != null) {
                if (fileDir!!.exists()) {
                    val result = RxZipTool.zipEncrypt(fileDir!!.absolutePath, fileZip!!.absolutePath, true, "123456")
                    tv_state.text = "压缩并加密成功,路径$result"
                } else {
                    RxToast.error("导出的文件不存在")
                }
            } else {
                RxToast.error("导出的文件不存在")
            }
        }
        btn_upzip.setOnClickListener {
            val zipFiles = RxZipTool.unzipFileByKeyword(fileZip, unZipDirFile, "123456")
            var str = "导出文件列表(*▽*)\n"
            if (zipFiles != null) {
                for (zipFile in zipFiles) {
                    str += """
                            ${zipFile.absolutePath}


                            """.trimIndent()
                }
            }
            tv_state.text = str
        }
        btn_zip_delete_dir.setOnClickListener {

            if (RxZipTool.removeDirFromZipArchive(zipPath, "RxTool" + File.separator + "RxTempTool")) {
                tv_state.text = "RxTempTool 删除成功"
            } else {
                tv_state.text = "RxTempTool 删除失败"
            }

        }

    }

    override fun initData() {

    }


    @SuppressLint("HandlerLeak")
    private val _handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RxZipTool.CompressStatus.START -> {
                    tv_state.text = "Start..."
                    Progress.visibility = View.VISIBLE
                }
                RxZipTool.CompressStatus.HANDLING -> {
                    val bundle = msg.data
                    val percent = bundle.getInt(RxZipTool.CompressKeys.PERCENT)
                    tv_state.text = "$percent%"
                    Progress.progress = percent
                }
                RxZipTool.CompressStatus.ERROR -> {
                    val bundle = msg.data
                    val error = bundle.getString(RxZipTool.CompressKeys.ERROR)
                    tv_state.text = error
                }
                RxZipTool.CompressStatus.COMPLETED -> {
                    tv_state.text = "Completed"
                    Progress.visibility = View.INVISIBLE
                }
                else -> {
                }
            }
        }
    }
}