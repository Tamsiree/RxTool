package com.tamsiree.rxkit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Xml
import android.view.Surface
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.tamsiree.rxkit.RxBarTool.getStatusBarHeight
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.view.RxToast
import org.json.JSONObject
import java.io.*
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * 设备工具类
 *
 * @author tamsiree
 * @date 2016/1/24
 */
object RxDeviceTool {
    /**
     * 得到屏幕的高
     *
     * @param context 实体
     * @return 设备屏幕的高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val wm = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        return wm.defaultDisplay.height
    }

    /**
     * 得到屏幕的宽
     *
     * @param context 实体
     * @return 设备屏幕的宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        return wm.defaultDisplay.width
    }

    /**
     * 得到设备屏幕的宽度
     */
    @JvmStatic
    fun getScreenWidths(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 得到设备屏幕的高度
     */
    @JvmStatic
    fun getScreenHeights(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 得到设备的密度
     */
    @JvmStatic
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 获取手机唯一标识序列号
     *
     * @return 手机唯一标识序列号
     */
    @JvmStatic
    val uniqueSerialNumber: String
        get() {
            //品牌类型 例如： Galaxy nexus
            val phoneName = Build.MODEL
            //品牌 例如：samsung
            val manuFacturer = Build.MANUFACTURER
            Log.d("详细序列号", "$manuFacturer-$phoneName-$serialNumber")
            return "$manuFacturer-$phoneName-$serialNumber"
        }

    /**
     * IMEI （唯一标识序列号）
     *
     * 需与[.isPhone]一起使用
     *
     * 需添加权限 `<uses-permission android:name="android.permission.READ_PHONE_STATE"/>`
     *
     * @param context 上下文
     * @return IMEI
     */
    @JvmStatic
    fun getIMEI(context: Context): String? {
        return if (isPhone(context)) {
            getDeviceIdIMEI(context)
        } else {
            getAndroidId(context)
        }
    }

    /**
     * 获取设备的IMSI
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getIMSI(context: Context): String? {
        return getSubscriberId(context)
    }

    /**
     * 获取设备的IMEI
     *
     * @param context
     * @return
     */
    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getDeviceIdIMEI(context: Context): String? {
        val id: String
        //android.telephony.TelephonyManager
        val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备权限")
            return null
        }
        id = if (mTelephony.deviceId != null) {
            mTelephony.deviceId
        } else {
            //android.provider.Settings;
            Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        }
        return id
    }

    /**
     * 获取设备的软件版本号
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getDeviceSoftwareVersion(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备权限")
            return null
        }
        return tm.deviceSoftwareVersion
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getLine1Number(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备权限")
            return null
        }
        return tm.line1Number
    }

    /**
     * 获取ISO标准的国家码，即国际长途区号
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkCountryIso(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso
    }

    /**
     * 获取设备的 MCC + MNC
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkOperator(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperator
    }

    /**
     * 获取(当前已注册的用户)的名字
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkOperatorName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }

    /**
     * 获取当前使用的网络类型
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkType
    }

    /**
     * 获取手机类型
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getPhoneType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType
    }

    /**
     * 获取SIM卡的国家码
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getSimCountryIso(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simCountryIso
    }

    /**
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getSimOperator(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simOperator
    }

    /**
     * 获取服务商名称
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getSimOperatorName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simOperatorName
    }

    /**
     * 获取SIM卡的序列号
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getSimSerialNumber(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备信息权限")
            return null
        }
        return tm.simSerialNumber
    }

    /**
     * 获取SIM的状态信息
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getSimState(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simState
    }

    /**
     * 获取唯一的用户ID
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getSubscriberId(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备信息权限")
            return null
        }
        return tm.subscriberId
    }

    /**
     * 获取语音邮件号码
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getVoiceMailNumber(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备信息权限")
            return null
        }
        return tm.voiceMailNumber
    }

    /**
     * 获取ANDROID ID
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }// Galaxy nexus 品牌类型

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    @JvmStatic
    val buildBrandModel: String
        get() = Build.MODEL // Galaxy nexus 品牌类型

    //google
    @JvmStatic
    val buildBrand: String
        get() = Build.BRAND //google
// samsung 品牌

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    @JvmStatic
    val buildMANUFACTURER: String
        get() = Build.MANUFACTURER // samsung 品牌

    /**
     * 序列号
     *
     * @return
     */
    @JvmStatic
    val serialNumber: String?
        @SuppressLint("PrivateApi", "HardwareIds")
        get() {
            var serial: String? = null
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)
                serial = get.invoke(c, "ro.serialno") as String
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return serial
        }

    val appPackageName: String
        get() = RxTool.getContext().packageName

    /**
     * 获取App版本名称
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getAppVersionName(context: Context?): String {
        // 获取packagemanager的实例
        val packageManager = context?.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager?.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo!!.versionName
    }

    /**
     * 获取App版本号
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getAppVersionNo(context: Context): Int {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo!!.versionCode
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission 例如 Manifest.permission.READ_PHONE_STATE
     * @return
     */
    @JvmStatic
    fun checkPermission(context: Context, permission: String): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= 23) {
            result = try {
                val clazz = Class.forName("android.content.Context")
                val method = clazz.getMethod("checkSelfPermission", String::class.java)
                val rest = method.invoke(context, permission) as Int
                rest == PackageManager.PERMISSION_GRANTED
            } catch (e: Exception) {
                false
            }
        } else {
            val pm = context.packageManager
            if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
                result = true
            }
        }
        return result
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getDeviceInfo(context: Context): String? {
        try {
            val json = JSONObject()
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var device_id: String? = null
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = getDeviceIdIMEI(context)
            }
            var mac: String? = null
            var fstream: FileReader? = null
            fstream = try {
                FileReader("/sys/class/net/wlan0/address")
            } catch (e: FileNotFoundException) {
                FileReader("/sys/class/net/eth0/address")
            }
            var `in`: BufferedReader? = null
            if (fstream != null) {
                try {
                    `in` = BufferedReader(fstream, 1024)
                    mac = `in`.readLine()
                } catch (e: IOException) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    if (`in` != null) {
                        try {
                            `in`.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            json.put("mac", mac)
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(context.contentResolver,
                        Settings.Secure.ANDROID_ID)
            }
            json.put("device_id", device_id)
            return json.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 遍历LOG输出HashMap
     *
     * @param res
     */
    @JvmStatic
    fun ThroughArray(res: HashMap<*, *>) {
        val ite: Iterator<*> = res.entries.iterator()
        while (ite.hasNext()) {
            val entry = ite.next() as Map.Entry<*, *>
            val key = entry.key!!
            val value = entry.value!!
            Log.d("MSG_AUTH_COMPLETE", "$key： $value")
        }
    }

    /**
     * 获取设备MAC地址
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
     *
     * @param context 上下文
     * @return MAC地址
     */
    @JvmStatic
    fun getMacAddress(context: Context): String? {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        if (info != null) {
            val macAddress = info.macAddress
            if (macAddress != null) {
                return macAddress.replace(":", "")
            }
        }
        return null
    }

    /**
     * 获取设备MAC地址
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
     *
     * @return MAC地址
     */
    val macAddress: String
        get() {
            var macAddress = ""
            var networkInterface: NetworkInterface? = null
            try {
                if (NetworkInterface.getByName("eth0") != null) {
                    networkInterface = NetworkInterface.getByName("eth0")
                } else if (NetworkInterface.getByName("wlan0") != null) {
                    networkInterface = NetworkInterface.getByName("wlan0")
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            if (networkInterface == null) {
                return macAddress
            }
            var macArr = ByteArray(0)
            try {
                macArr = networkInterface.hardwareAddress
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            val buf = StringBuilder()
            for (b in macArr) {
                buf.append(String.format("%02X", b))
            }
            macAddress = buf.toString()
            Log.d("mac", "interfaceName=" + networkInterface.name + ", mac=" + macAddress)
            macAddress = macAddress.replace(":", "")
            return macAddress
        }

    /**
     * 判断设备是否是手机
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isPhone(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 获取手机状态信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.READ_PHONE_STATE"/>`
     *
     * @param context 上下文
     * @return DeviceId(IMEI) = 99000311726612<br></br>
     * DeviceSoftwareVersion = 00<br></br>
     * Line1Number =<br></br>
     * NetworkCountryIso = cn<br></br>
     * NetworkOperator = 46003<br></br>
     * NetworkOperatorName = 中国电信<br></br>
     * NetworkType = 6<br></br>
     * honeType = 2<br></br>
     * SimCountryIso = cn<br></br>
     * SimOperator = 46003<br></br>
     * SimOperatorName = 中国电信<br></br>
     * SimSerialNumber = 89860315045710604022<br></br>
     * SimState = 5<br></br>
     * SubscriberId(IMSI) = 460030419724900<br></br>
     * VoiceMailNumber = *86<br></br>
     */
    @JvmStatic
    fun getPhoneStatus(context: Context): String? {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            RxToast.error("请先获取读取手机设备信息权限")
            return null
        }
        val tm = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var str = ""
        str += """
            DeviceId(IMEI) = ${getDeviceIdIMEI(context)}

            """.trimIndent()
        str += """
            DeviceSoftwareVersion = ${tm.deviceSoftwareVersion}

            """.trimIndent()
        str += """
            Line1Number = ${tm.line1Number}

            """.trimIndent()
        str += """
            NetworkCountryIso = ${tm.networkCountryIso}

            """.trimIndent()
        str += """
            NetworkOperator = ${tm.networkOperator}

            """.trimIndent()
        str += """
            NetworkOperatorName = ${tm.networkOperatorName}

            """.trimIndent()
        str += """
            NetworkType = ${tm.networkType}

            """.trimIndent()
        str += """
            honeType = ${tm.phoneType}

            """.trimIndent()
        str += """
            SimCountryIso = ${tm.simCountryIso}

            """.trimIndent()
        str += """
            SimOperator = ${tm.simOperator}

            """.trimIndent()
        str += """
            SimOperatorName = ${tm.simOperatorName}

            """.trimIndent()
        str += """
            SimSerialNumber = ${tm.simSerialNumber}

            """.trimIndent()
        str += """
            SimState = ${tm.simState}

            """.trimIndent()
        str += """
            SubscriberId(IMSI) = ${tm.subscriberId}

            """.trimIndent()
        str += """
            VoiceMailNumber = ${tm.voiceMailNumber}

            """.trimIndent()
        return str
    }

    /**
     * 跳至填充好phoneNumber的拨号界面
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    @JvmStatic
    fun dial(context: Context, phoneNumber: String) {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
    }

    /**
     * 拨打电话
     * 需添加权限 `<uses-permission android:name="android.permission.CALL_PHONE"/>`
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    @JvmStatic
    fun callPhone(context: Context, phoneNumber: String) {
        if (!isNullString(phoneNumber)) {
            val phoneNumber1 = phoneNumber.trim { it <= ' ' } // 删除字符串首部和尾部的空格
            // 调用系统的拨号服务实现电话拨打功能
            // 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber1"))
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            context.startActivity(intent) // 内部类
        }
    }

    /**
     * 发送短信
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     * @param content     内容
     */
    @JvmStatic
    fun sendSms(context: Context, phoneNumber: String?, content: String?) {
        val uri = Uri.parse("smsto:" + if (isNullString(phoneNumber)) "" else phoneNumber)
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", if (isNullString(content)) "" else content)
        context.startActivity(intent)
    }

    /**
     * 获取手机联系人
     *
     * 需添加权限 `<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.READ_CONTACTS"/>`
     *
     * @param context 上下文;
     * @return 联系人链表
     */
    @JvmStatic
    fun getAllContactInfo(context: Context): List<HashMap<String, String>> {
        SystemClock.sleep(3000)
        val list = ArrayList<HashMap<String, String>>()
        // 1.获取内容解析者
        val resolver = context.contentResolver
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        val raw_uri = Uri.parse("content://com.android.contacts/raw_contacts")
        val date_uri = Uri.parse("content://com.android.contacts/data")
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        val cursor = resolver.query(raw_uri, arrayOf("contact_id"),
                null, null, null)
        // 5.解析cursor
        while (cursor!!.moveToNext()) {
            // 6.获取查询的数据
            val contact_id = cursor.getString(0)
            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
            // 判断contact_id是否为空
            if (!isNullString(contact_id)) { //null   ""
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                val c = resolver.query(date_uri, arrayOf("data1",
                        "mimetype"), "raw_contact_id=?", arrayOf(contact_id), null)
                val map = HashMap<String, String>()
                // 8.解析c
                while (c!!.moveToNext()) {
                    // 9.获取数据
                    val data1 = c.getString(0)
                    val mimetype = c.getString(1)
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype == "vnd.android.cursor.item/phone_v2") {
                        // 电话
                        map["phone"] = data1
                    } else if (mimetype == "vnd.android.cursor.item/name") {
                        // 姓名
                        map["name"] = data1
                    }
                }
                // 11.添加到集合中数据
                list.add(map)
                // 12.关闭cursor
                c.close()
            }
        }
        // 12.关闭cursor
        cursor.close()
        return list
    }

    /**
     * 打开手机联系人界面点击联系人后便获取该号码
     *
     * 参照以下注释代码
     */
    @JvmStatic
    fun getContantNum(context: Activity) {
        Log.i("tips", "U should copy the following code.")
        val intent = Intent()
        intent.action = "android.intent.action.PICK"
        intent.type = "vnd.android.cursor.dir/phone_v2"
        context.startActivityForResult(intent, 0)

        /*@Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                Uri uri = data.getData();
                String num = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(uri,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    num = cursor.getString(cursor.getColumnIndex("data1"));
                }
                cursor.close();
                num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
            }
        }*/
    }

    /**
     * 获取手机短信并保存到xml中
     *
     * 需添加权限 `<uses-permission android:name="android.permission.READ_SMS"/>`
     *
     * 需添加权限 `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>`
     *
     * @param context 上下文
     */
    @JvmStatic
    fun getAllSMS(context: Context) {
        // 1.获取短信
        // 1.1获取内容解析者
        val resolver = context.contentResolver
        // 1.2获取内容提供者地址   sms,sms表的地址:null  不写
        // 1.3获取查询路径
        val uri = Uri.parse("content://sms")
        // 1.4.查询操作
        // projection : 查询的字段
        // selection : 查询的条件
        // selectionArgs : 查询条件的参数
        // sortOrder : 排序
        val cursor = resolver.query(uri, arrayOf("address", "date", "type", "body"), null, null, null)
        // 设置最大进度
        val count = cursor!!.count //获取短信的个数
        // 2.备份短信
        // 2.1获取xml序列器
        val xmlSerializer = Xml.newSerializer()
        try {
            // 2.2设置xml文件保存的路径
            // os : 保存的位置
            // encoding : 编码格式
            xmlSerializer.setOutput(FileOutputStream(File("/mnt/sdcard/backupsms.xml")), "utf-8")
            // 2.3设置头信息
            // standalone : 是否独立保存
            xmlSerializer.startDocument("utf-8", true)
            // 2.4设置根标签
            xmlSerializer.startTag(null, "smss")
            // 1.5.解析cursor
            while (cursor.moveToNext()) {
                SystemClock.sleep(1000)
                // 2.5设置短信的标签
                xmlSerializer.startTag(null, "sms")
                // 2.6设置文本内容的标签
                xmlSerializer.startTag(null, "address")
                val address = cursor.getString(0)
                // 2.7设置文本内容
                xmlSerializer.text(address)
                xmlSerializer.endTag(null, "address")
                xmlSerializer.startTag(null, "date")
                val date = cursor.getString(1)
                xmlSerializer.text(date)
                xmlSerializer.endTag(null, "date")
                xmlSerializer.startTag(null, "type")
                val type = cursor.getString(2)
                xmlSerializer.text(type)
                xmlSerializer.endTag(null, "type")
                xmlSerializer.startTag(null, "body")
                val body = cursor.getString(3)
                xmlSerializer.text(body)
                xmlSerializer.endTag(null, "body")
                xmlSerializer.endTag(null, "sms")
                println("address:$address   date:$date  type:$type  body:$body")
            }
            xmlSerializer.endTag(null, "smss")
            xmlSerializer.endDocument()
            // 2.8将数据刷新到文件中
            xmlSerializer.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置屏幕为横屏
     *
     * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
     *
     * 不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
     *
     * 设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
     *
     * 设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
     *
     * @param activity activity
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @JvmStatic
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @JvmStatic
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 判断是否横屏
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 判断是否竖屏
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    @JvmStatic
    fun getScreenRotation(activity: Activity): Int {
        return when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    @JvmStatic
    fun captureWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        val ret = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return ret
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * 需要用到上面获取状态栏高度getStatusBarHeight的方法
     *
     * @param activity activity
     * @return Bitmap
     */
    @JvmStatic
    fun captureWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val statusBarHeight = getStatusBarHeight(activity)
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        val ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return ret
    }

    /**
     * 获取DisplayMetrics对象
     *
     * @param context 应用程序上下文
     * @return
     */
    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    /**
     * 判断是否锁屏
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isScreenLock(context: Context): Boolean {
        val km = context
                .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return km.inKeyguardRestrictedInputMode()
    }

    /**
     * 设置安全窗口，禁用系统截屏。防止 App 中的一些界面被截屏，并显示在其他设备中造成信息泄漏。
     * （常见手机设备系统截屏操作方式为：同时按下电源键和音量键。）
     *
     * @param activity
     */
    @JvmStatic
    fun noScreenshots(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}