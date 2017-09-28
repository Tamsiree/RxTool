package com.vondear.tools.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxFileTool;
import com.vondear.rxtools.RxZipTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.tools.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityZipEncrypt extends ActivityBase {

    @BindView(R.id.btn_create_folder)
    Button mBtnCreateFolder;
    @BindView(R.id.btn_zip)
    Button mBtnZip;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    private File fileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_encrypt);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R.id.btn_create_folder, R.id.btn_zip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_folder:
                fileDir = new File(RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxTools");
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                mTvState.setText("文件夹 RxTools 创建成功,文件夹位于在根目录");
                break;
            case R.id.btn_zip:
                File fileZip = new File(RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxTools.zip");
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
        }
    }
}
