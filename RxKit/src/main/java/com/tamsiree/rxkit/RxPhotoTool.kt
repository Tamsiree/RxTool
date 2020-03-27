package com.tamsiree.rxkit

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tamsiree.rxkit.RxFileTool.Companion.getDataColumn
import com.tamsiree.rxkit.RxFileTool.Companion.isDownloadsDocument
import com.tamsiree.rxkit.RxFileTool.Companion.isExternalStorageDocument
import com.tamsiree.rxkit.RxFileTool.Companion.isGooglePhotosUri
import com.tamsiree.rxkit.RxFileTool.Companion.isMediaDocument
import com.tamsiree.rxkit.view.RxToast
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 */
object RxPhotoTool {
    const val GET_IMAGE_BY_CAMERA = 5001
    const val GET_IMAGE_FROM_PHONE = 5002
    const val CROP_IMAGE = 5003

    @JvmField
    var imageUriFromCamera: Uri? = null

    @JvmField
    var cropImageUri: Uri? = null

    @JvmStatic
    fun openCameraImage(activity: Activity) {
        imageUriFromCamera = createImagePathUri(activity)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        activity.startActivityForResult(intent, GET_IMAGE_BY_CAMERA)
    }

    @JvmStatic
    fun openCameraImage(fragment: Fragment) {
        imageUriFromCamera = createImagePathUri(fragment.context)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        fragment.startActivityForResult(intent, GET_IMAGE_BY_CAMERA)
    }

    @JvmStatic
    fun openLocalImage(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(intent, GET_IMAGE_FROM_PHONE)
    }

    @JvmStatic
    fun openLocalImage(fragment: Fragment) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(intent, GET_IMAGE_FROM_PHONE)
    }

    @JvmStatic
    fun cropImage(activity: Activity, srcUri: Uri?) {
        cropImageUri = createImagePathUri(activity)
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(srcUri, "image/*")
        intent.putExtra("crop", "true")

        ////////////////////////////////////////////////////////////////
        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        ////////////////////////////////////////////////////////////////
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        ////////////////////////////////////////////////////////////////
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        ////////////////////////////////////////////////////////////////
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        //	会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        //  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
        ////////////////////////////////////////////////////////////////

        // aspectX aspectY 是裁剪框宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪后生成图片的宽高
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri)
        intent.putExtra("return-data", true)
        activity.startActivityForResult(intent, CROP_IMAGE)
    }

    @JvmStatic
    fun cropImage(fragment: Fragment, srcUri: Uri?) {
        cropImageUri = createImagePathUri(fragment.context)
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(srcUri, "image/*")
        intent.putExtra("crop", "true")

        ////////////////////////////////////////////////////////////////
        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        ////////////////////////////////////////////////////////////////
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        ////////////////////////////////////////////////////////////////
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        ////////////////////////////////////////////////////////////////
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        //	会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        //  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
        ////////////////////////////////////////////////////////////////

        // aspectX aspectY 是裁剪框宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪后生成图片的宽高
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri)
        intent.putExtra("return-data", true)
        fragment.startActivityForResult(intent, CROP_IMAGE)
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    @JvmStatic
    fun createImagePathUri(context: Context?): Uri? {
        val imageFilePath = arrayOf<Uri?>(null)
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            imageFilePath[0] = Uri.parse("")
            RxToast.error("请先获取写入SDCard权限")
        } else {
            val status = Environment.getExternalStorageState()
            val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
            val time = System.currentTimeMillis()
            val imageName = timeFormatter.format(Date(time))
            // ContentValues是我们希望这条记录被创建时包含的数据信息
            val values = ContentValues(3)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            values.put(MediaStore.Images.Media.DATE_TAKEN, time)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (status == Environment.MEDIA_MOUNTED) { // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
                imageFilePath[0] = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                imageFilePath[0] = context.contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values)
            }
        }
        TLog.i("", "生成的照片输出路径：" + imageFilePath[0].toString())
        return imageFilePath[0]
    }

    //此方法 只能用于4.4以下的版本
    @JvmStatic
    fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)

//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @JvmStatic
    @TargetApi(19)
    fun getImageAbsolutePath(context: Context?, imageUri: Uri?): String? {
        if (context == null || imageUri == null) {
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) {
                imageUri.lastPathSegment
            } else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }
        return null
    }
}