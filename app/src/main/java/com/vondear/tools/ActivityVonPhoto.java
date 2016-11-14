package com.vondear.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vondear.vontools.VonPhotoUtils;
import com.vondear.vontools.VonSPUtils;
import com.vondear.vontools.view.TransparentDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActivityVonPhoto extends AppCompatActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_von_photo);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogOpenAvatar();
            }
        });
    }

    private void initDialogOpenAvatar() {
        final TransparentDialog dialog1 = new TransparentDialog(this);
        dialog1.getAttr().gravity = Gravity.BOTTOM;
        View dialogView1 = LayoutInflater.from(this).inflate(
                R.layout.dialog_picker_pictrue, null);
        TextView tv_camera = (TextView) dialogView1
                .findViewById(R.id.tv_camera);
        TextView tv_file = (TextView) dialogView1
                .findViewById(R.id.tv_file);
        TextView tv_cancelid = (TextView) dialogView1
                .findViewById(R.id.tv_cancelid);
        tv_cancelid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog1.cancel();
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                VonPhotoUtils.openCameraImage(ActivityVonPhoto.this);
                dialog1.cancel();

            }
        });
        tv_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                VonPhotoUtils.openLocalImage(ActivityVonPhoto.this);
                dialog1.cancel();
            }
        });
        dialog1.setContentView(dialogView1);
        dialog1.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case VonPhotoUtils.GET_IMAGE_FROM_PHONE:
                if (resultCode == RESULT_OK) {
//                    VonPhotoUtils.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case VonPhotoUtils.GET_IMAGE_BY_CAMERA:
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    VonPhotoUtils.cropImage(ActivityUser.this, VonPhotoUtils.imageUriFromCamera);// 裁剪图片
                    initUCrop(VonPhotoUtils.imageUriFromCamera);
                }

                break;
            case VonPhotoUtils.CROP_IMAGE:
                Glide.with(context).
                        load(VonPhotoUtils.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(context)).
                        thumbnail(0.5f).
                        placeholder(R.drawable.elves_ball).
                        priority(Priority.LOW).
                        error(R.drawable.elves_ball).
                        fallback(R.drawable.elves_ball).
                        dontAnimate().
                        into(ivAvatar);
//                RequestUpdateAvatar(new File(VonPhotoUtils.getRealFilePath(context, VonPhotoUtils.cropImageUri)));
              /*  if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    //--------------------------------
*//*                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 -
                        // 100)压缩文件
                        // imageView.setImageBitmap(photo);
                        ivAvatar.setImageBitmap(photo);
                        File file = new File(Environment.getExternalStorageDirectory()
                                + "/imgHead.jpg");// 将要保存图片的路径
                        OutputStream stream11 = null;
                        try {
                            stream11 = new FileOutputStream(Environment.getExternalStorageDirectory() + "/imgHead.jpg");
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream11);*//*
                    //=================================

                    if (head != null) {
                        *//**
             * 上传服务器代码
             *//*
                        // setPicToView(head);// 保存在SD卡中
                        ivAvatar.setImageBitmap(head);// 用ImageView显示出来
                    }
                }*/
                break;

            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    Glide.with(context).
                            load(resultUri).
                            diskCacheStrategy(DiskCacheStrategy.RESULT).
                            bitmapTransform(new CropCircleTransformation(context)).
                            thumbnail(0.5f).
                            placeholder(R.drawable.elves_ball).
                            priority(Priority.LOW).
                            error(R.drawable.elves_ball).
                            fallback(R.drawable.elves_ball).
                            dontAnimate().
                            into(ivAvatar);
//                    RequestUpdateAvatar(new File(VonPhotoUtils.getRealFilePath(context, resultUri)));

                    VonSPUtils.putContent(context, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUCrop(Uri uri) {

//        Uri destinationUri = VonPhotoUtils.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
//        options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
//        options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
//        options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
//        options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
//        options.setCropGridColumnCount(2);
        //设置横线的数量
//        options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }
}
