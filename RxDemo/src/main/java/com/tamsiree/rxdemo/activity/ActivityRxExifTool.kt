package com.tamsiree.rxdemo.activity

import android.Manifest
import android.location.Location
import android.os.Bundle
import com.tamsiree.camera.RxCameraView
import com.tamsiree.camera.tool.RxCameraTool
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.*
import com.tamsiree.rxkit.interfaces.OnRxCamera
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBaseLocation
import com.tamsiree.rxui.view.dialog.RxDialogScaleView
import kotlinx.android.synthetic.main.activity_rx_exif_tool.*
import java.io.File
import java.util.*

/**
 * @author tamsiree
 */
class ActivityRxExifTool : ActivityBaseLocation() {

    private var photo: File? = null

    override fun setGpsInfo(location: Location?) {
        tv_gps.text = String.format("经度: %s  纬度: %s\n精度: %s  方位: %s", RxLocationTool.gpsToDegree(location!!.longitude), RxLocationTool.gpsToDegree(location.latitude), location.accuracy, location.bearing)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        RxBarTool.setTransparentStatusBar(this)
        setContentView(R.layout.activity_rx_exif_tool)
        RxDeviceTool.setPortrait(this)

    }

    override fun initView() {

        initCamera() //初始化相机

        btn_take_camera.setOnClickListener {
            if (RxTool.isFastClick(1000)) {
                RxToast.normal("请不要重复点击拍照按钮")
                return@setOnClickListener
            } else {
                RxCameraTool.takePic(mContext, camera)
            }
        }
        iv_pic.setOnClickListener {
            if (photo == null) {
                RxToast.normal("请先拍照")
            } else {
                val rxDialogScaleView = RxDialogScaleView(mContext)
                rxDialogScaleView.setImage(photo!!.absolutePath, false)
                rxDialogScaleView.show()
            }
        }
    }

    override fun initData() {
        RxPermissionsTool.with(mContext).addPermission(Manifest.permission.ACCESS_FINE_LOCATION).addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).addPermission(Manifest.permission.CAMERA).addPermission(Manifest.permission.READ_PHONE_STATE).initPermission()
    }

    private fun initCamera() {
        camera.addCallback(object : RxCameraView.Callback() {

            override fun onPictureTaken(cameraView: RxCameraView, data: ByteArray) {
                super.onPictureTaken(cameraView, data)
                initCameraEvent(data)
            }
        })
    }

    private fun initCameraEvent(data: ByteArray) {
        val fileDir = RxFileTool.rootPath?.absolutePath + File.separator + "RoadExcel" + File.separator + "picture"
        val fileName = RxTimeTool.getCurrentDateTime("yyyyMMddHHmmss") + "_" + Random().nextInt(1000) + ".jpg"
        RxCameraTool.initCameraEvent(mContext, camera, data, fileDir, fileName, mLongitude, mLatitude, false, object : OnRxCamera {
            override fun onBefore() {
                tv_state.text = "拍照成功,开始压缩\n"
            }

            override fun onSuccessCompress(file: File?) {
                tv_state.text = String.format("%s图片压缩成功\n", tv_state.text)
                photo = file
                iv_pic.setImageURI(RxFileTool.getImageContentUri(mContext, photo))
            }

            override fun onSuccessExif(filePhoto: File?) {
                tv_state.text = String.format("%s地理位置信息写入图片成功\n", tv_state.text)
                photo = filePhoto
                iv_pic.setImageURI(RxFileTool.getImageContentUri(mContext, photo))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (permission == Manifest.permission.CAMERA) {
                camera.start()
            }
        }
    }

}