package com.vondear.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vondear.rxdemo.R;
import com.vondear.rxtool.RxFileTool;
import com.vondear.rxtool.RxZipTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxTitle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author vondear
 */
public class ActivityZipEncrypt extends ActivityBase {

    @BindView(R.id.btn_create_folder)
    Button mBtnCreateFolder;
    @BindView(R.id.btn_zip)
    Button mBtnZip;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.btn_upzip)
    Button mBtnUpzip;

    private File fileDir;
    private File unZipDirFile;
    private File fileZip;

    private String zipPath;
    private String zipParentPath;
    private String unzipPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_encrypt);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
        zipParentPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxTool";
        unzipPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxToolUnZip";
        zipPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "Rxtool.zip";

        unZipDirFile = new File(unzipPath);
        if (!unZipDirFile.exists()) {
            unZipDirFile.mkdirs();
        }
    }

    @OnClick({R.id.btn_create_folder, R.id.btn_zip, R.id.btn_upzip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_folder:
                fileDir = new File(zipParentPath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                try {
                    File file = File.createTempFile("RxTool_Temp", ".txt", fileDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mTvState.setText("文件夹 RxTool 创建成功,文件夹位于在根目录");
                break;
            case R.id.btn_zip:
                fileZip = new File(zipPath);
                if (fileZip.exists()) {
                    RxFileTool.deleteFile(fileZip);
                    Logger.d("导出文件已存在，将之删除");
                }

                if (fileDir != null) {
                    if (fileDir.exists()) {
                        String result = RxZipTool.zipEncrypt(fileDir.getAbsolutePath(), fileZip.getAbsolutePath(), true, "123456");
                        mTvState.setText("压缩并加密成功,路径" + result);
                    } else {
                        RxToast.error("导出的文件不存在");
                    }
                } else {
                    RxToast.error("导出的文件不存在");
                }
                break;
            case R.id.btn_upzip:
                try {
                    List<File> zipFiles = RxZipTool.unzipFileByKeyword(fileZip, unZipDirFile, "123456");
                    String str = "导出文件列表\n";
                    for (File zipFile : zipFiles) {
                        str += zipFile.getAbsolutePath() + "\n";
                    }
                    mTvState.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
