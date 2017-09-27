package com.vondear.rxtools.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.vondear.rxtools.RxAnimationTool;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.interfaces.OnRxScanerListener;
import com.vondear.rxtools.module.scaner.BitmapLuminanceSource;
import com.vondear.rxtools.module.scaner.CameraManager;
import com.vondear.rxtools.module.scaner.CaptureActivityHandler;
import com.vondear.rxtools.module.scaner.decoding.InactivityTimer;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSure;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import static com.vondear.rxtools.RxConstants.SP_SCAN_CODE;

public class ActivityScanerCode extends ActivityBase implements SurfaceHolder.Callback {

    private static final float BEEP_VOLUME = 0.50f;
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + File.separator + "fengci/";
    private static final long VIBRATE_DURATION = 200L;
    private static OnRxScanerListener mScanerListener;
    private final int CHOOSE_PICTURE = 1003;
    boolean flag = true;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int mCropWidth = 0;
    private int mCropHeight = 0;
    private RelativeLayout mContainer = null;
    private RelativeLayout mCropLayout = null;
    private LinearLayout ll_scan_help;//生成二维码 & 条形码 布局
    private ImageView mIvLight;//闪光灯 按钮
    private RxDialogSure rxDialogSure;
    //----------------------------------------------------------------------------------------------解析结果 及 后续处理 start
    private String mResult;

    public static void setScanerListener(OnRxScanerListener scanerListener) {
        mScanerListener = scanerListener;
    }

    /*
     * 保存文件
     */
    public static String saveFile(Bitmap bm, String fileName) throws IOException {
        String path;
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        path = myCaptureFile.getAbsolutePath();
        FileOutputStream fileOutputStream = new FileOutputStream(myCaptureFile);
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            if (options > 10) {
                options -= 10;// 每次都减少10
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                break;
            }
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return path;
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 设置文件名称
     *
     * @return
     */
    public static String setImageName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
        return simpleDateFormat.format(new Date()) + ".jpg";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.setNoTitle(this);
        setContentView(com.vondear.rxtools.R.layout.activity_scaner_code);
        RxBarTool.setTransparentStatusBar(this);
        initView();//界面控件初始化
        initScanerAnimation();//扫描动画初始化
        CameraManager.init(mContext);//初始化 CameraManager
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(com.vondear.rxtools.R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);//Camera初始化
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        mScanerListener = null;
        super.onDestroy();
    }

    private void initView() {
        mIvLight = (ImageView) findViewById(com.vondear.rxtools.R.id.top_mask);
        mContainer = (RelativeLayout) findViewById(com.vondear.rxtools.R.id.capture_containter);
        mCropLayout = (RelativeLayout) findViewById(com.vondear.rxtools.R.id.capture_crop_layout);
        ll_scan_help = (LinearLayout) findViewById(com.vondear.rxtools.R.id.ll_scan_help);
        ll_scan_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // RxActivityTool.skipActivity(ActivityScanerCode.this, ActivityCreateQRCode.class);
            }
        });
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        rxDialogSure = new RxDialogSure(mContext);//提示弹窗
        rxDialogSure.getTitleView().setText("扫描结果");
    }

    private void initScanerAnimation() {
        ImageView mQrLineView = (ImageView) findViewById(com.vondear.rxtools.R.id.capture_scan_line);
        RxAnimationTool.ScaleUpDowm(mQrLineView);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;

    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    private void light() {
        if (flag) {
            flag = false;
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
            CameraManager.get().offLight();
        }

    }

    //----------------------------------------------------------------------------------------------打开本地图片识别二维码 start

    public void btn(View view) {
        int i = view.getId();
        if (i == com.vondear.rxtools.R.id.top_mask) {
            light();
        } else if (i == com.vondear.rxtools.R.id.top_back) {
            finish();
        } else if (i == com.vondear.rxtools.R.id.top_openpicture) {
            getPicture();
        } else {

        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.y;
            int height = point.x;
            int x = mCropLayout.getLeft() * width / mContainer.getWidth();
            int y = mCropLayout.getTop() * height / mContainer.getHeight();
            int cropWidth = mCropLayout.getWidth() * width
                    / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height
                    / mContainer.getHeight();
            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(ActivityScanerCode.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    /***
     * 调用系统相册
     */
    private void getPicture() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }
    //==============================================================================================打开本地图片识别二维码 end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / 2, photo.getHeight() / 2);// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    photo.recycle(); // 释放原始图片占用的内存，防止out of memory异常发生
//                    String bitmappath = saveFile(smallBitmap, setImageName());

                    MultiFormatReader multiFormatReader = new MultiFormatReader();

                    // 解码的参数
                    Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
                    // 可以解析的编码类型
                    Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
                    if (decodeFormats == null || decodeFormats.isEmpty()) {
                        decodeFormats = new Vector<BarcodeFormat>();

                        Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
                        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
                        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
                        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
                        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
                        // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
                        Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
                        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
                        ONE_D_FORMATS.add(BarcodeFormat.ITF);
                        Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
                        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
                        Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
                        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

                        // 这里设置可扫描的类型，我这里选择了都支持
                        decodeFormats.addAll(ONE_D_FORMATS);
                        decodeFormats.addAll(QR_CODE_FORMATS);
                        decodeFormats.addAll(DATA_MATRIX_FORMATS);
                    }
                    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
                    // 设置继续的字符编码格式为UTF8
                    // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
                    // 设置解析配置参数
                    multiFormatReader.setHints(hints);

                    // 开始对图像资源解码
                    Result rawResult = null;
                    try {
                        rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(smallBitmap))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (rawResult != null) {
                        if (mScanerListener == null) {
                            dialogShow(rawResult);
                        } else {
                            mScanerListener.onSuccess("From to Picture", rawResult);
                        }
                    } else {
                        if (mScanerListener == null) {
                            RxToast.error("图片识别失败.");
                        } else {
                            mScanerListener.onFail("From to Picture", "图片识别失败");
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dialogShow(Result result) {
        String type = result.getBarcodeFormat().name();
        String realContent = result.getText();
        if ("QR_CODE".equals(type)) {
            rxDialogSure.getTitleView().setText("二维码扫描结果");
        } else if ("EAN_13".equals(type)) {
            rxDialogSure.getTitleView().setText("条形码扫描结果");
        }

        rxDialogSure.setContent(realContent);
        rxDialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSure.cancel();
            }
        });
        rxDialogSure.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
                handler.sendEmptyMessage(com.vondear.rxtools.R.id.restart_preview);
            }
        });
        rxDialogSure.show();

        RxSPTool.putContent(mContext, SP_SCAN_CODE, RxDataTool.stringToInt(RxSPTool.getContent(mContext, SP_SCAN_CODE)) + 1 + "");
    }
    //==============================================================================================解析结果 及 后续处理 end

    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        this.mResult = result.getText();
        Log.v("二维码/条形码 扫描结果", mResult);
        if (mScanerListener == null) {
            RxToast.success(mResult);
            dialogShow(result);
        } else {
            mScanerListener.onSuccess("From to Camera", result);
        }
    }

    //----------------------------------------------------------------------------------------------扫描成功之后的振动与声音提示 start
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });

            AssetFileDescriptor file = getResources().openRawResourceFd(com.vondear.rxtools.R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    //==============================================================================================扫描成功之后的振动与声音提示 end
}