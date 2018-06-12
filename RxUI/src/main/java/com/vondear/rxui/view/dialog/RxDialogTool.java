package com.vondear.rxui.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.vondear.rxtool.RxIntentTool;
import com.vondear.rxui.R;

/**
 * @author vondear
 * @date 2018/4/11 10:40:00
 */
public class RxDialogTool {

    /**
     * 加载进度 提示弹窗
     */
    @SuppressLint("StaticFieldLeak")
    private static RxDialogLoading mDialogLoading;

    /**
     * 耗时操作 提示弹窗
     */
    @SuppressLint("StaticFieldLeak")
    private static RxDialogLoading mDialogLoad;

    /**
     * 网络请求加载框
     */
    public static void loadingHttp(Context context) {
        if (mDialogLoading != null) {
            mDialogLoading.cancel();
        }
        mDialogLoading = new RxDialogLoading(context);
        mDialogLoading.setCanceledOnTouchOutside(false);
        mDialogLoading.setCancelable(false);
        mDialogLoading.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen));
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    /**
     * 网络请求加载框
     * @param context
     * @param hint 提示语
     */
    public static void loadingHttp(Context context, String hint) {
        if (mDialogLoading != null) {
            mDialogLoading.cancel();
        }
        mDialogLoading = new RxDialogLoading(context);
        mDialogLoading.setCanceledOnTouchOutside(false);
        mDialogLoading.setCancelable(false);
        mDialogLoading.setLoadingText(hint);
        mDialogLoading.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen));
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    /**
     * 网络请求加载框 取消
     */
    public static void loadingHttpCancel() {
        if (mDialogLoading != null) {
            mDialogLoading.cancel();
        }
    }

    /**
     * 网络请求加载框 取消
     */
    public static void loadingHttpCancel(String reminder) {
        if (mDialogLoading != null) {
            mDialogLoading.cancel(reminder);
        }
    }

    /**
     * 耗时操作加载框
     */
    public static void loading(Context context) {
        if (mDialogLoad != null) {
            mDialogLoad.cancel();
        }
        mDialogLoad = new RxDialogLoading(context);
        mDialogLoad.setCanceledOnTouchOutside(false);
        mDialogLoad.setCancelable(false);
        mDialogLoad.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen));
        mDialogLoad.setLoadingText("正在进行操作..");
        if (!mDialogLoad.isShowing()) {
            mDialogLoad.show();
        }
    }

    /**
     * 耗时操作加载框
     * @param context
     * @param hint 提示语
     */
    public static void loading(Context context, String hint) {
        if (mDialogLoad != null) {
            mDialogLoad.cancel();
        }
        mDialogLoad = new RxDialogLoading(context);
        mDialogLoad.setCanceledOnTouchOutside(false);
        mDialogLoad.setCancelable(false);
        mDialogLoad.setLoadingColor(ContextCompat.getColor(context, R.color.lightseagreen));
        mDialogLoad.setLoadingText(hint);
        if (!mDialogLoad.isShowing()) {
            mDialogLoad.show();
        }
    }

    /**
     * 耗时操作加载框 取消
     */
    public static void loadingCancel() {
        if (mDialogLoad == null) {

        } else {
            mDialogLoad.cancel();
        }
    }

    /**
     * 跳转系统设置APP权限界面
     *
     * @param mContext
     * @param str
     */
    public static void initDialogSurePermission(final Context mContext, String str) {
        final RxDialogSure rxDialogSure = new RxDialogSure(mContext);
        rxDialogSure.getLogoView().setVisibility(View.GONE);
        rxDialogSure.getTitleView().setVisibility(View.GONE);
        rxDialogSure.setContent(str);
        rxDialogSure.getContentView().setTextSize(20);
        rxDialogSure.getContentView().setTextColor(ContextCompat.getColor(mContext, R.color.SUCCESS_COLOR));
        rxDialogSure.getContentView().setGravity(Gravity.CENTER);
        rxDialogSure.setCanceledOnTouchOutside(false);
        rxDialogSure.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSure.cancel();
                mContext.startActivity(RxIntentTool.getAppDetailsSettingsIntent(mContext));
            }
        });
        rxDialogSure.show();
    }

    /**
     * 显示大图
     *
     * @param context
     * @param uri     图片的Uri
     */
    public static void showBigImageView(Context context, Uri uri) {
        final RxDialog rxDialog = new RxDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.image, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialog.cancel();
            }
        });
        ImageView imageView = view.findViewById(R.id.page_item);

        imageView.setImageURI(uri);

        rxDialog.setContentView(view);
        rxDialog.show();
        rxDialog.setFullScreen();
    }
}
