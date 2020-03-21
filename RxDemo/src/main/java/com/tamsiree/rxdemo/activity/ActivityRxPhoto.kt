package com.tamsiree.rxdemo.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxPhotoTool
import com.tamsiree.rxkit.RxPhotoTool.getImageAbsolutePath
import com.tamsiree.rxkit.RxSPTool.putContent
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage
import com.tamsiree.rxui.view.dialog.RxDialogChooseImage.LayoutType
import com.tamsiree.rxui.view.dialog.RxDialogScaleView
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author tamsiree
 */
class ActivityRxPhoto : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.tv_bg)
    var mTvBg: TextView? = null

    @JvmField
    @BindView(R.id.iv_avatar)
    var mIvAvatar: ImageView? = null

    @JvmField
    @BindView(R.id.ll_anchor_left)
    var mLlAnchorLeft: LinearLayout? = null

    @JvmField
    @BindView(R.id.rl_avatar)
    var mRlAvatar: RelativeLayout? = null

    @JvmField
    @BindView(R.id.tv_name)
    var mTvName: TextView? = null

    @JvmField
    @BindView(R.id.tv_constellation)
    var mTvConstellation: TextView? = null

    @JvmField
    @BindView(R.id.tv_birthday)
    var mTvBirthday: TextView? = null

    @JvmField
    @BindView(R.id.tv_address)
    var mTvAddress: TextView? = null

    @JvmField
    @BindView(R.id.tv_lables)
    var mTvLables: TextView? = null

    @JvmField
    @BindView(R.id.textView2)
    var mTextView2: TextView? = null

    @JvmField
    @BindView(R.id.editText2)
    var mEditText2: TextView? = null

    @JvmField
    @BindView(R.id.btn_exit)
    var mBtnExit: Button? = null

    @JvmField
    @BindView(R.id.activity_user)
    var mActivityUser: LinearLayout? = null
    private var resultUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_rxphototool)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    protected fun initView() {
        val r = mContext!!.resources
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceEntryName(R.drawable.circle_elves_ball))
        mRxTitle!!.setLeftFinish(mContext)
        mIvAvatar!!.setOnClickListener { initDialogChooseImage() }
        mIvAvatar!!.setOnLongClickListener { //                RxImageTool.showBigImageView(mContext, resultUri);
            val rxDialogScaleView = RxDialogScaleView(mContext)
            rxDialogScaleView.setImage(resultUri)
            rxDialogScaleView.show()
            false
        }
    }

    private fun initDialogChooseImage() {
        val dialogChooseImage = RxDialogChooseImage(mContext, LayoutType.NO_TITLE)
        dialogChooseImage.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RxPhotoTool.GET_IMAGE_FROM_PHONE -> if (resultCode == Activity.RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                initUCrop(data!!.data)
            }
            RxPhotoTool.GET_IMAGE_BY_CAMERA -> if (resultCode == Activity.RESULT_OK) {
                /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                initUCrop(RxPhotoTool.imageUriFromCamera)
            }
            RxPhotoTool.CROP_IMAGE -> {
                val options = RequestOptions()
                        .placeholder(R.drawable.circle_elves_ball) //异常占位图(当加载异常的时候出现的图片)
                        .error(R.drawable.circle_elves_ball) //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                Glide.with(mContext!!).load(RxPhotoTool.cropImageUri).apply(options).thumbnail(0.5f).into(mIvAvatar!!)
            }
            UCrop.REQUEST_CROP -> if (resultCode == Activity.RESULT_OK) {
                resultUri = UCrop.getOutput(data!!)
                roadImageView(resultUri, mIvAvatar)
                putContent(mContext!!, "AVATAR", resultUri.toString())
            } else if (resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(data!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private fun roadImageView(uri: Uri?, imageView: ImageView?): File {
        val options = RequestOptions()
                .placeholder(R.drawable.circle_elves_ball) //异常占位图(当加载异常的时候出现的图片)
                .error(R.drawable.circle_elves_ball)
                .transform(CircleCrop()) //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(mContext!!).load(uri).apply(options).thumbnail(0.5f).into(imageView!!)
        return File(getImageAbsolutePath(this, uri))
    }

    private fun initUCrop(uri: Uri?) {
        val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val time = System.currentTimeMillis()
        val imageName = timeFormatter.format(Date(time))
        val destinationUri = Uri.fromFile(File(cacheDir, "$imageName.jpeg"))
        val options = UCrop.Options()
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary))
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5f)
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666)
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);
        UCrop.of(uri!!, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this)
    }

    @OnClick(R.id.btn_exit)
    fun onClick() {
        val rxDialogSureCancel = RxDialogSureCancel(this)
        rxDialogSureCancel.cancelView.setOnClickListener { rxDialogSureCancel.cancel() }
        rxDialogSureCancel.sureView.setOnClickListener { finish() }
        rxDialogSureCancel.show()
    }
}