package com.tamsiree.rxui.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.tamsiree.rxkit.RxIntentTool;
import com.tamsiree.rxkit.RxVibrateTool;
import com.tamsiree.rxkit.view.RxToast;
import com.tamsiree.rxui.R;
import com.tamsiree.rxui.adapter.AdapterCardViewModelPicture;
import com.tamsiree.rxui.model.ModelPicture;
import com.tamsiree.rxui.view.cardstack.RxCardStackView;

import java.util.List;

/**
 * @author tamsiree
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
        rxDialogSure.getContentView().setTextColor(ContextCompat.getColor(mContext, R.color.green_388e3c));
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

    /**
     * 提示框
     *
     * @param hint 提示的内容
     */
    public static void initDialogExport(final Context mContext, final String hint) {
        RxVibrateTool.vibrateOnce(mContext, 150);
        final RxDialogSureCancel mDialogExport = new RxDialogSureCancel(mContext, R.style.PushUpInDialogThem);
        mDialogExport.getTitleView().setBackground(null);
        mDialogExport.getTitleView().setText("数据导出目录");
        mDialogExport.setContent(hint);
        mDialogExport.getContentView().setTextSize(13f);
        mDialogExport.getSureView().setVisibility(View.GONE);
        mDialogExport.setCancel("确定");
        mDialogExport.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxVibrateTool.vibrateOnce(mContext, 150);
                mDialogExport.cancel();
            }
        });
        mDialogExport.setCancelable(false);
        mDialogExport.show();
    }

    public static void initDialogShowPicture(Context mContext,final List<ModelPicture> modelList) {
        final RxDialog mDialogShowPicture = new RxDialog(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_picture, null);
        final RxCardStackView mStackView = dialogView.findViewById(R.id.stackview_main);
        final LinearLayout mButtonContainer = dialogView.findViewById(R.id.button_container);
        Button btnPre = dialogView.findViewById(R.id.btn_Pre);
        Button btnNext = dialogView.findViewById(R.id.btn_next);
        mStackView.setItemExpendListener(new RxCardStackView.ItemExpendListener() {
            @Override
            public void onItemExpend(boolean expend) {
                mButtonContainer.setVisibility(expend ? View.VISIBLE : View.INVISIBLE);
            }
        });
        AdapterCardViewModelPicture testStackAdapter = new AdapterCardViewModelPicture(mContext);
        mStackView.setAdapter(testStackAdapter);

        testStackAdapter.updateData(modelList);

        mDialogShowPicture.setContentView(dialogView);
        mDialogShowPicture.setFullScreen();

        if (modelList.size() > 1) {
            btnPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStackView.getSelectPosition() == 0) {
                        RxToast.info("当前为第一张");
                    } else {
                        mStackView.pre();
                    }
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStackView.getSelectPosition() == modelList.size() - 1) {
                        RxToast.info("当前为最后一张");
                    } else {
                        mStackView.next();
                    }
                }
            });
            btnPre.setText("上一张");
            btnNext.setVisibility(View.VISIBLE);
            btnPre.setVisibility(View.VISIBLE);
        } else {
            btnPre.setText("确定");
            btnPre.setVisibility(View.VISIBLE);
            btnPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogShowPicture.cancel();
                }
            });
            btnNext.setVisibility(View.GONE);
        }
        testStackAdapter.updateData(modelList);
        mDialogShowPicture.show();
    }

}
