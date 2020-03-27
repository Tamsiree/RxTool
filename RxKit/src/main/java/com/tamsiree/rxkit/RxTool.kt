package com.tamsiree.rxkit

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.tamsiree.rxkit.RxVibrateTool.vibrateOnce
import com.tamsiree.rxkit.interfaces.OnDoListener
import com.tamsiree.rxkit.interfaces.OnSimpleListener
import com.tamsiree.rxkit.view.RxToast.normal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * @author tamsiree
 * @date 2016/1/24
 * RxTool的常用工具类
 *
 *
 * For the brave souls who get this far: You are the chosen ones,
 * the valiant knights of programming who toil away, without rest,
 * fixing our most awful code. To you, true saviors, kings of men,
 * I say this: never gonna give you up, never gonna let you down,
 * never gonna run around and desert you. Never gonna make you cry,
 * never gonna say goodbye. Never gonna tell a lie and hurt you.
 *
 *
 * 致终于来到这里的勇敢的人：
 * 你是被上帝选中的人，是英勇的、不敌辛苦的、不眠不休的来修改我们这最棘手的代码的编程骑士。
 * 你，我们的救世主，人中之龙，我要对你说：永远不要放弃，永远不要对自己失望，永远不要逃走，辜负了自己，
 * 永远不要哭啼，永远不要说再见，永远不要说谎来伤害自己。
 */
object RxTool {

    @SuppressLint("StaticFieldLeak")
    private var context: Context? = null
    private var lastClickTime: Long = 0

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    @JvmStatic
    fun init(context: Context): RxTool {
        RxTool.context = context.applicationContext
        //        RxCrashTool.init(context);
        TLog.init(context)
        return RxTool
    }

    /**
     * 在某种获取不到 Context 的情况下，即可以使用才方法获取 Context
     *
     *
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    @JvmStatic
    fun getContext(): Context {
        if (context != null) {
            return context as Context
        }
        throw NullPointerException("请先调用init()方法")
    }

    fun debugLog(switch: Boolean): RxTool {
        TLog.switchLog(switch)
        return RxTool
    }

    fun debugLogFile(switch: Boolean): RxTool {
        TLog.switch2File(switch)
        return RxTool
    }

    fun crashLogFile(switch: Boolean): RxTool {
        TLog.switchCrashFile(switch)
        return RxTool
    }

    //----------------------------------------------------------------------------------------------延时任务封装 start
    @JvmStatic
    fun delayToDo(delayTime: Long, onSimpleListener: OnSimpleListener) {
        Handler().postDelayed({ //execute the task
            onSimpleListener.doSomething()
        }, delayTime)
    }
    //==============================================================================================延时任务封装 end

    /**
     * 倒计时
     *
     * @param textView 控件
     * @param waitTime 倒计时总时长
     * @param interval 倒计时的间隔时间
     * @param hint     倒计时完毕时显示的文字
     */
    @JvmStatic
    fun countDown(textView: TextView, waitTime: Long, interval: Long, hint: String?) {
        textView.isEnabled = false
        val timer: CountDownTimer = object : CountDownTimer(waitTime, interval) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                textView.text = String.format("剩下 %d S", millisUntilFinished / 1000)
            }

            override fun onFinish() {
                textView.isEnabled = true
                textView.text = hint
            }
        }
        timer.start()
    }

    /**
     * 手动计算出listView的高度，但是不再具有滚动效果
     *
     * @param listView
     */
    @JvmStatic
    fun fixListViewHeight(listView: ListView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        val listAdapter = listView.adapter
        var totalHeight = 0
        if (listAdapter == null) {
            return
        }
        var index = 0
        val len = listAdapter.count
        while (index < len) {
            val listViewItem = listAdapter.getView(index, null, listView)
            // 计算子项View 的宽高
            listViewItem.measure(0, 0)
            // 计算所有子项的高度
            totalHeight += listViewItem.measuredHeight
            index++
        }
        val params = listView.layoutParams
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }
    //---------------------------------------------MD5加密-------------------------------------------
    /**
     * 生成MD5加密32位字符串
     *
     * @param MStr :需要加密的字符串
     * @return
     */
    @JvmStatic
    fun Md5(MStr: String): String {
        return try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(MStr.toByteArray())
            bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            MStr.hashCode().toString()
        }
    }

    // MD5内部算法---------------不能修改!
    private fun bytesToHexString(bytes: ByteArray): String {
        // http://stackoverflow.com/questions/332079
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
    //============================================MD5加密============================================
    /**
     * 根据资源名称获取资源 id
     *
     *
     * 不提倡使用这个方法获取资源,比其直接获取ID效率慢
     *
     *
     * 例如
     * getResources().getIdentifier("ic_launcher", "drawable", getPackageName());
     *
     * @param context
     * @param name
     * @param defType
     * @return
     */
    @JvmStatic
    fun getResIdByName(context: Context, name: String?, defType: String?): Int {
        return context.resources.getIdentifier(name, defType, context.packageName)
    }

    @JvmStatic
    fun isFastClick(millisecond: Int): Boolean {
        val curClickTime = System.currentTimeMillis()
        val interval = curClickTime - lastClickTime
        if (0 < interval && interval < millisecond) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            return true
        }
        lastClickTime = curClickTime
        return false
    }

    /**
     * Edittext 首位小数点自动加零，最多两位小数
     *
     * @param editText
     */
    @JvmStatic
    fun setEdTwoDecimal(editText: EditText) {
        setEdDecimal(editText, 2)
    }

    /**
     * 只允许数字和汉字
     * @param editText
     */
    @JvmStatic
    fun setEdType(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val editable = editText.text.toString()
                val str = stringFilter(editable)
                if (editable != str) {
                    editText.setText(str)
                    //设置新的光标所在位置
                    editText.setSelection(str.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    /**
     * // 只允许数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    @Throws(PatternSyntaxException::class)
    @JvmStatic
    fun stringFilter(str: String?): String {
        val regEx = "[^0-9\u4E00-\u9FA5]" //正则表达式
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim { it <= ' ' }
    }

    @JvmStatic
    fun setEdDecimal(editText: EditText, count: Int) {
        var count = count
        if (count < 0) {
            count = 0
        }
        count += 1
        editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER

        //设置字符过滤
        val finalCount = count
        editText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (".".contentEquals(source) && dest.toString().length == 0) {
                return@InputFilter "0."
            }
            if (dest.toString().contains(".")) {
                val index = dest.toString().indexOf(".")
                val mlength = dest.toString().substring(index).length
                if (mlength == finalCount) {
                    return@InputFilter ""
                }
            }
            if (dest.toString() == "0" && source == "0") {
                ""
            } else null
        })
    }

    /**
     * @param editText       输入框控件
     * @param number         位数
     * 1 -> 1
     * 2 -> 01
     * 3 -> 001
     * 4 -> 0001
     * @param isStartForZero 是否从000开始
     * true -> 从 000 开始
     * false -> 从 001 开始
     */
    @JvmStatic
    fun setEditNumberAuto(editText: EditText, number: Int, isStartForZero: Boolean) {
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                setEditNumber(editText, number, isStartForZero)
            }
        }
    }

    /**
     * @param editText       输入框控件
     * @param number         位数
     * 1 -> 1
     * 2 -> 01
     * 3 -> 001
     * 4 -> 0001
     * @param isStartForZero 是否从000开始
     * true -> 从 000 开始
     * false -> 从 001 开始
     */
    @JvmStatic
    fun setEditNumber(editText: EditText, number: Int, isStartForZero: Boolean) {
        var s = StringBuilder(editText.text.toString())
        val temp = StringBuilder()
        var i: Int
        i = s.length
        while (i < number) {
            s.insert(0, "0")
            ++i
        }
        if (!isStartForZero) {
            i = 0
            while (i < number) {
                temp.append("0")
                ++i
            }
            if (s.toString() == temp.toString()) {
                s = StringBuilder(temp.substring(1) + "1")
            }
        }
        editText.setText(s.toString())
    }

    /**
     * 获取
     * @return
     */
    val backgroundHandler: Handler
        get() {
            val thread = HandlerThread("background")
            thread.start()
            return Handler(thread.looper)
        }

    @JvmStatic
    fun initFastClickAndVibrate(mContext: Context?, onRxSimple: OnDoListener) {
        if (isFastClick(RxConstants.FAST_CLICK_TIME)) {
            normal("请不要重复点击")
            return
        } else {
            vibrateOnce(mContext!!, RxConstants.VIBRATE_TIME)
            onRxSimple.doSomething()
        }
    }
}