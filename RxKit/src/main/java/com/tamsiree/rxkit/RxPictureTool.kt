package com.tamsiree.rxkit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxImageTool.save
import java.io.File
import java.io.IOException

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 * 相机相关工具类
 */
object RxPictureTool {
    /**
     * 获取打开照程序界面的Intent
     */
    val openCameraIntent: Intent get() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    /**
     * 获取跳转至相册选择界面的Intent
     */
    val imagePickerIntent: Intent
        get() {
            val intent = Intent(Intent.ACTION_PICK, null)
            return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }

    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
     */
    @JvmStatic
    fun getImagePickerIntent(outputX: Int, outputY: Int, fromFileURI: Uri?,
                             saveFileURI: Uri?): Intent {
        return getImagePickerIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI)
    }

    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
     */
    @JvmStatic
    fun getImagePickerIntent(aspectX: Int, aspectY: Int, outputX: Int, outputY: Int, fromFileURI: Uri?,
                             saveFileURI: Uri?): Intent {
        return getImagePickerIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI)
    }

    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，可以指定是否缩放裁剪区域]的Intent
     *
     * @param aspectX     裁剪框尺寸比例X
     * @param aspectY     裁剪框尺寸比例Y
     * @param outputX     输出尺寸宽度
     * @param outputY     输出尺寸高度
     * @param canScale    是否可缩放
     * @param fromFileURI 文件来源路径URI
     * @param saveFileURI 输出文件路径URI
     */
    @JvmStatic
    fun getImagePickerIntent(aspectX: Int, aspectY: Int, outputX: Int, outputY: Int, canScale: Boolean,
                             fromFileURI: Uri?, saveFileURI: Uri?): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(fromFileURI, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", if (aspectX <= 0) 1 else aspectX)
        intent.putExtra("aspectY", if (aspectY <= 0) 1 else aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", canScale)
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        // 去除人脸识别
        return intent.putExtra("noFaceDetection", true)
    }

    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
     */
    @JvmStatic
    fun getCameraIntent(saveFileURI: Uri?): Intent {
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return mIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI)
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     */
    @JvmStatic
    fun getCropImageIntent(outputX: Int, outputY: Int, fromFileURI: Uri?,
                           saveFileURI: Uri?): Intent {
        return getCropImageIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI)
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     */
    @JvmStatic
    fun getCropImageIntent(aspectX: Int, aspectY: Int, outputX: Int, outputY: Int, fromFileURI: Uri?,
                           saveFileURI: Uri?): Intent {
        return getCropImageIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI)
    }

    /**
     * 获取[跳转至裁剪界面]的Intent
     */
    @JvmStatic
    fun getCropImageIntent(aspectX: Int, aspectY: Int, outputX: Int, outputY: Int, canScale: Boolean,
                           fromFileURI: Uri?, saveFileURI: Uri?): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(fromFileURI, "image/*")
        intent.putExtra("crop", "true")
        // X方向上的比例
        intent.putExtra("aspectX", if (aspectX <= 0) 1 else aspectX)
        // Y方向上的比例
        intent.putExtra("aspectY", if (aspectY <= 0) 1 else aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", canScale)
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("return-data", false)
        // 需要将读取的文件路径和裁剪写入的路径区分，否则会造成文件0byte
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI)
        // true-->返回数据类型可以设置为Bitmap，但是不能传输太大，截大图用URI，小图用Bitmap或者全部使用URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true)
        return intent
    }

    /**
     * 获得选中相册的图片
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     * @return bitmap
     */
    @JvmStatic
    fun getChoosedImage(context: Activity, data: Intent?): Bitmap? {
        if (data == null) return null
        var bm: Bitmap? = null
        val cr = context.contentResolver
        val originalUri = data.data
        try {
            bm = MediaStore.Images.Media.getBitmap(cr, originalUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bm
    }

    /**
     * 获得选中相册的图片路径
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     * @return
     */
    @JvmStatic
    fun getChoosedImagePath(context: Activity, data: Intent?): String? {
        if (data == null) return null
        var path = ""
        val resolver = context.contentResolver
        val originalUri = data.data ?: return null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = resolver.query(originalUri, projection, null, null, null)
        if (null != cursor) {
            try {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                path = cursor.getString(column_index)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                try {
                    if (!cursor.isClosed) {
                        cursor.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return if (isNullString(path)) originalUri.path else null
    }

    /**
     * 获取拍照之后的照片文件（JPG格式）
     *
     * @param data     onActivityResult回调返回的数据
     * @param filePath 文件路径
     * @return 文件
     */
    @JvmStatic
    fun getTakePictureFile(data: Intent?, filePath: String?): File? {
        if (data == null) return null
        val extras = data.extras ?: return null
        val photo = extras.getParcelable<Bitmap>("data")
        val file = File(filePath)
        return if (save(photo!!, file, Bitmap.CompressFormat.JPEG)) file else null
    }
}