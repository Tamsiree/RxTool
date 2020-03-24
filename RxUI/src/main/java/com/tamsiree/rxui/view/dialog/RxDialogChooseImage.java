package com.tamsiree.rxui.view.dialog;

import android.app.Activity;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tamsiree.rxkit.RxPhotoTool;
import com.tamsiree.rxui.R;


/**
 * @author tamsiree
 * @date 2017/3/20
 * 封装了从相册/相机 获取 图片 的Dialog.
 */
public class RxDialogChooseImage extends RxDialog {

    private LayoutType mLayoutType = LayoutType.TITLE;
    private TextView mTvCamera;
    private TextView mTvFile;
    private Button btnCancel;
    private TextView tvOriginalImage;
    private Uri uriOriginalImage;

    public RxDialogChooseImage(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment) {
        super(fragment.getContext());
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, Uri uri) {
        super(context);
        uriOriginalImage = uri;
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, Uri uri) {
        super(fragment.getContext());
        uriOriginalImage = uri;
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, int themeResId) {
        super(fragment.getContext(), themeResId);
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, float alpha, int gravity) {
        super(fragment.getContext(), alpha, gravity);
        initView(fragment);
    }

    public RxDialogChooseImage(Fragment fragment, LayoutType layoutType) {
        super(fragment.getContext());
        mLayoutType = layoutType;
        initView(fragment);
    }


    public RxDialogChooseImage(Activity context, LayoutType layoutType) {
        super(context);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Activity context, int themeResId, LayoutType layoutType) {
        super(context, themeResId);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, int themeResId, LayoutType layoutType) {
        super(fragment.getContext(), themeResId);
        mLayoutType = layoutType;
        initView(fragment);
    }

    public RxDialogChooseImage(Activity context, float alpha, int gravity, LayoutType layoutType) {
        super(context, alpha, gravity);
        mLayoutType = layoutType;
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment, float alpha, int gravity, LayoutType layoutType) {
        super(fragment.getContext(), alpha, gravity);
        mLayoutType = layoutType;
        initView(fragment);
    }

    private void initView(final Activity activity) {
        init();
        setClickEvent(activity);
    }

    private void initView(final Fragment fragment) {
        init();
        setClickEvent(fragment);
    }

    private void init() {
        View dialogView = null;
        switch (mLayoutType) {
            case TITLE:
                dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
            default:
                break;
        }

        tvOriginalImage = dialogView.findViewById(R.id.tv_original_image);
        mTvCamera = dialogView.findViewById(R.id.tv_camera);
        mTvFile = dialogView.findViewById(R.id.tv_file);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        tvOriginalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriOriginalImage != null) {
                    RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext, uriOriginalImage);
                    rxDialogScaleView.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });

        setContentView(dialogView);
        mLayoutParams.gravity = Gravity.BOTTOM;
        mLayoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void setClickEvent(Activity activity) {
        mTvCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoTool.openCameraImage(activity);
                cancel();
            }
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoTool.openLocalImage(activity);
                cancel();
            }
        });
    }


    private void setClickEvent(Fragment fragment) {
        mTvCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //请求Camera权限
                RxPhotoTool.openCameraImage(fragment);
                cancel();
            }
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoTool.openLocalImage(fragment);
                cancel();
            }
        });
    }

    public TextView getCancelView() {
        return btnCancel;
    }

    public TextView getFromCameraView() {
        return mTvCamera;
    }

    public TextView getFromFileView() {
        return mTvFile;
    }

    public TextView getTvOriginalImage() {
        return tvOriginalImage;
    }

    public LayoutType getLayoutType() {
        return mLayoutType;
    }

    public void setTvOriginalImage(TextView tvOriginalImage) {
        this.tvOriginalImage = tvOriginalImage;
    }

    public Uri getUriOriginalImage() {
        return uriOriginalImage;
    }

    public void setUriOriginalImage(Uri uriOriginalImage) {
        this.uriOriginalImage = uriOriginalImage;
    }

    public enum LayoutType {
        TITLE, NO_TITLE
    }
}
