package com.vondear.tools.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.tools.R;


/**
 * @author vondear
 */
public class ActivitySplash extends ActivityBase {

    ProgressBar pg;
    boolean update = false;
    private TextView tv_splash_version;
    private TextView tv_update_info;
    private Context context;
    private String appVersionName;
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

    private Handler checkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!update) {
                RxToast.showToast(context, "正在检查版本更新...", 500);
                // TODO: 使用 RxDeviceTool.getAppVersionNo(context); 方法 获取当前app版本号 与 提交给服务器 做对比
                String temp = getResources().getString(R.string.newest_apk_down);
                String timeTip = String.format(temp, RxDeviceTool.getAppVersionName(context));
                //  或简化成 String.format(getResources().getString(R.string.newest_apk_down),RxDeviceTool.getAppVersionName(context))
                ShowDialog(timeTip, "your_apk_down_url");
            } else {
                RxToast.showToast(context, "当前为最新版本，无需更新!", 500);
                pg.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.hideStatusBar(this);//隐藏状态栏 并 全屏
        setContentView(R.layout.activity_splash);
        context = this;
        initView();
        CheckUpdate();

    }

    private void initView() {
        pg = (ProgressBar) findViewById(R.id.pg);
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        appVersionName = RxDeviceTool.getAppVersionName(context);
        tv_splash_version.setText("版本号 "+appVersionName);
    }

    public void buttonClick(View v) {
        RxToast.showToast(context, "正在进入主界面", 500);
        toMain();
    }

    public void toMain() {
        Intent intent = new Intent();
        intent.setClass(context, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void CheckUpdate() {
        new Thread() {
            public void run() {
                Message msg = checkhandler.obtainMessage();
                checkhandler.sendMessage(msg);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                update = true;
                checkhandler.sendMessage(new Message());
            }
        }.start();
    }

    private void ShowDialog(String strAppVersionName, String apk_down_url) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);//提示弹窗
        rxDialogSureCancel.getContentView().setText(strAppVersionName);
        rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getFile(apk_down_url, RxFileTool.getDiskFileDir(context) + File.separator + "update", str + ".apk");
                // TODO: 第一步 在此处 使用 你的网络框架下载 新的Apk文件 可参照下面的例子 使用的是 okGo网络框架
                // TODO: 第二步 可使用 RxAppTool.InstallAPK(context,file.getAbsolutePath()); 方法进行 最新版本Apk文件的安装
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.showToast(context,"已取消最新版本的下载",500);
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }
}
