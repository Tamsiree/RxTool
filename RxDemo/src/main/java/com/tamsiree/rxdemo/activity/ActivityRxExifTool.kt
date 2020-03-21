package com.tamsiree.rxdemo.activity

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.camera.RxCameraView
import com.tamsiree.camera.tool.RxCameraTool
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.*
import com.tamsiree.rxkit.interfaces.OnRxCamera
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBaseLocation
import com.tamsiree.rxui.view.dialog.RxDialogScaleView
import java.io.File
import java.util.*

/**
 * @author tamsiree
 */
class ActivityRxExifTool : ActivityBaseLocation() {
    @JvmField
    @BindView(R.id.camera)
    var mCameraView: RxCameraView? = null

    @JvmField
    @BindView(R.id.btn_take_camera)
    var mBtnTakeCamera: Button? = null

    @JvmField
    @BindView(R.id.tv_gps)
    var mTvGps: TextView? = null

    @JvmField
    @BindView(R.id.tv_state)
    var mTvState: TextView? = null

    @JvmField
    @BindView(R.id.iv_pic)
    var mIvPic: ImageView? = null
    private var photo: File? = null
    override fun setGpsInfo(location: Location) {
        mTvGps!!.text = String.format("经度: %s  纬度: %s\n精度: %s  方位: %s", RxLocationTool.gpsToDegree(location.longitude), RxLocationTool.gpsToDegree(location.latitude), location.accuracy, location.bearing)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        RxBarTool.setTransparentStatusBar(this)
        setContentView(R.layout.activity_rx_exif_tool)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
        RxPermissionsTool.with(mContext).addPermission(Manifest.permission.ACCESS_FINE_LOCATION).addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).addPermission(Manifest.permission.CAMERA).addPermission(Manifest.permission.READ_PHONE_STATE).initPermission()
        initCamera() //初始化相机
    }

    private fun initCamera() {
        mCameraView!!.addCallback(object : RxCameraView.Callback() {

            override fun onPictureTaken(cameraView: RxCameraView, data: ByteArray) {
                super.onPictureTaken(cameraView, data)
                initCameraEvent(data)
            }
        })
    }

    private fun initCameraEvent(data: ByteArray) {
        val fileDir = RxFileTool.rootPath?.absolutePath + File.separator + "RoadExcel" + File.separator + "picture"
        val fileName = RxTimeTool.getCurrentDateTime("yyyyMMddHHmmss") + "_" + Random().nextInt(1000) + ".jpg"
        RxCameraTool.initCameraEvent(mContext, mCameraView, data, fileDir, fileName, mLongitude, mLatitude, false, object : OnRxCamera {
            override fun onBefore() {
                mTvState!!.text = "拍照成功,开始压缩\n"
            }

            override fun onSuccessCompress(file: File?) {
                mTvState!!.text = String.format("%s图片压缩成功\n", mTvState!!.text)
                photo = file
                mIvPic!!.setImageURI(RxFileTool.getImageContentUri(mContext, photo))
            }

            override fun onSuccessExif(filePhoto: File?) {
                mTvState!!.text = String.format("%s地理位置信息写入图片成功\n", mTvState!!.text)
                photo = filePhoto
                mIvPic!!.setImageURI(RxFileTool.getImageContentUri(mContext, photo))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mCameraView!!.start()
    }

    override fun onPause() {
        mCameraView!!.stop()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (permission == Manifest.permission.CAMERA) {
                mCameraView!!.start()
            }
        }
    }

    @OnClick(R.id.btn_take_camera, R.id.iv_pic)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_take_camera -> if (RxTool.isFastClick(1000)) {
                RxToast.normal("请不要重复点击拍照按钮")
                return
            } else {
                RxCameraTool.takePic(mContext, mCameraView)
            }
            R.id.iv_pic -> if (photo == null) {
                RxToast.normal("请先拍照")
            } else {
                val rxDialogScaleView = RxDialogScaleView(mContext)
                rxDialogScaleView.setImage(photo!!.absolutePath, false)
                rxDialogScaleView.show()
            }
            else -> {
            }
        }
    }
}