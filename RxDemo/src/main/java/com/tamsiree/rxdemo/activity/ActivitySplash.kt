package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.activity.ActivityMain
import com.tamsiree.rxkit.RxBarTool.hideStatusBar
import com.tamsiree.rxkit.RxDeviceTool.getAppVersionName
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel

/**
 * @author tamsiree
 */
@SuppressLint("HandlerLeak")
class ActivitySplash : ActivityBase() {
    var pg: ProgressBar? = null
    var update = false
    private var tv_splash_version: TextView? = null
    private var tv_update_info: TextView? = null
    private var context: Context? = null
    private var appVersionName: String? = null

    /**
     * 例子
     * 下载APk文件并自动弹出安装
     */
    /*    public void getFile(String url, final String filePath, String name) {
        OkGo.get(url)//
                .tag(this)//
                .execute(new FileCallback(filePath, name) {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
                        context.startActivity(i);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }*/
    private val checkhandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (!update) {
                RxToast.showToast(context, "正在检查版本更新...", 500)
                // TODO: 使用 RxDeviceTool.getAppVersionNo(context); 方法 获取当前app版本号 与 提交给服务器 做对比
                val temp = resources.getString(R.string.newest_apk_down)
                val timeTip = String.format(temp, getAppVersionName(context))
                //  或简化成 String.format(getResources().getString(R.string.newest_apk_down),RxDeviceTool.getAppVersionName(context))
                ShowDialog(timeTip, "your_apk_down_url")
            } else {
                RxToast.showToast(context, "当前为最新版本，无需更新!", 500)
                pg!!.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(this) //隐藏状态栏 并 全屏
        setContentView(R.layout.activity_splash)
        setPortrait(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        pg = findViewById(R.id.pg)
        tv_update_info = findViewById(R.id.tv_update_info)
        tv_splash_version = findViewById(R.id.tv_splash_version)
        appVersionName = getAppVersionName(context)
        tv_splash_version?.text = "版本号 $appVersionName"
    }

    override fun initData() {
        CheckUpdate()
    }

    fun buttonClick(v: View?) {
        RxToast.showToast(context, "正在进入主界面", 500)
        toMain()
    }

    fun toMain() {
        val intent = Intent()
        intent.setClass(context!!, ActivityMain::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private fun CheckUpdate() {
        object : Thread() {
            override fun run() {
                val msg = checkhandler.obtainMessage()
                checkhandler.sendMessage(msg)
                try {
                    sleep(2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                update = true
                checkhandler.sendMessage(Message())
            }
        }.start()
    }

    private fun ShowDialog(strAppVersionName: String, apk_down_url: String) {
        val rxDialogSureCancel = RxDialogSureCancel(context) //提示弹窗
        rxDialogSureCancel.contentView.text = strAppVersionName
        rxDialogSureCancel.sureView.setOnClickListener { //getFile(apk_down_url, RxFileTool.getDiskFileDir(context) + File.separator + "update", str + ".apk");
            // TODO: 第一步 在此处 使用 你的网络框架下载 新的Apk文件 可参照下面的例子 使用的是 okGo网络框架
            // TODO: 第二步 可使用 RxAppTool.InstallAPK(context,file.getAbsolutePath()); 方法进行 最新版本Apk文件的安装
            rxDialogSureCancel.cancel()
        }
        rxDialogSureCancel.cancelView.setOnClickListener {
            RxToast.showToast(context, "已取消最新版本的下载", 500)
            rxDialogSureCancel.cancel()
        }
        rxDialogSureCancel.show()
    }
}