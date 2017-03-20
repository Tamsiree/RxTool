package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxPermissionsUtils;
import com.vondear.rxtools.RxPhotoUtils;
import com.vondear.rxtools.interfaces.onRequestListener;


/**
 * Created by vondear on 2017/3/20.
 * 封装了从相册/相机 获取 图片 的Dialog.
 */
public class RxDialogChooseImage extends RxDialog {

    public enum LayoutType {
        TITLE, NO_TITLE
    }

    private LayoutType mLayoutType = LayoutType.TITLE;

    private TextView tv_camera;
    private TextView tv_file;
    private TextView tv_cancel;

    public TextView getTv_camera() {
        return tv_camera;
    }

    public TextView getTv_file() {
        return tv_file;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public LayoutType getLayoutType() {
        return mLayoutType;
    }

    private void initView(final Activity context) {
        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }


        tv_camera = (TextView) dialog_view.findViewById(R.id.tv_camera);
        tv_file = (TextView) dialog_view.findViewById(R.id.tv_file);
        tv_cancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //请求Camera权限
                RxPermissionsUtils.requestCamera(context, new onRequestListener() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onRequestLater() {
                        RxPhotoUtils.openCameraImage(context);
                        cancel();
                    }
                });
            }
        });
        tv_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoUtils.openLocalImage(context);
                cancel();
            }
        });
        setContentView(dialog_view);
    }

    private void initView(final Fragment fragment) {
        View dialog_view = null;
        switch (mLayoutType) {
            case TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picker_pictrue, null);
                break;
            case NO_TITLE:
                dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_camero_show, null);
                break;
        }

        tv_camera = (TextView) dialog_view.findViewById(R.id.tv_camera);
        tv_file = (TextView) dialog_view.findViewById(R.id.tv_file);
        tv_cancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //请求Camera权限
                RxPermissionsUtils.requestCamera(fragment.getContext(), new onRequestListener() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onRequestLater() {
                        RxPhotoUtils.openCameraImage(fragment);
                        cancel();
                    }
                });
            }
        });
        tv_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RxPhotoUtils.openLocalImage(fragment);
                cancel();
            }
        });
        setContentView(dialog_view);
    }


    public RxDialogChooseImage(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogChooseImage(Fragment fragment) {
        super(fragment.getContext());
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
}
