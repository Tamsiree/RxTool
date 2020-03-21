package com.tamsiree.rxkit

import android.content.Context

/**
 * SharedPreferences工具类
 *
 * @author tamsiree
 * @date 2016/1/24
 */
object RxSPTool {
    private const val JSON_CACHE = "JSON_CACHE"

    /**
     * 存入自定义的标识的数据 可以近似的看作网络下载数据的缓存
     * 单条方式存入
     *
     * @param context 使用的上下文
     * @param tag     存入内容的标记，约定俗成的tag用当前的类名命名来区分不同的sp
     * @param content 存入的内
     */
    @JvmStatic
    fun putContent(context: Context, tag: String?, content: String?) {
        putString(context, tag, content)
    }

    /**
     * 获取以tag命名的存储内
     *
     * @param context 当前调用的上下文
     * @param tag     命名的tag
     * @return 返回以tag区分的内容，默认为空
     */
    @JvmStatic
    fun getContent(context: Context, tag: String?): String {
        return getString(context, tag)
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    @JvmStatic
    fun putString(context: Context, key: String?, value: String?) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmStatic
    fun getString(context: Context, key: String?): String {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val value: String
        value = sp.getString(key, "")!!
        return value
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    @JvmStatic
    fun putInt(context: Context, key: String?, value: Int) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    @JvmStatic
    fun getInt(context: Context, key: String?): Int {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val value: Int
        value = sp.getInt(key, -1)
        return value
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    @JvmStatic
    fun putLong(context: Context, key: String?, value: Long) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    @JvmStatic
    fun getLong(context: Context, key: String?): Long {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val value: Long
        value = sp.getLong(key, -1L)
        return value
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    @JvmStatic
    fun putFloat(context: Context, key: String?, value: Float) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    @JvmStatic
    fun getFloat(context: Context, key: String?): Float {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val value: Float
        value = sp.getFloat(key, -1f)
        return value
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    @JvmStatic
    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmStatic
    fun getBoolean(context: Context, key: String?): Boolean {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val value: Boolean
        value = sp.getBoolean(key, false)
        return value
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    @JvmStatic
    fun remove(context: Context, key: String?) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        sp.edit().remove(key).apply()
    }

    /**
     * 存放JSON缓存数据
     *
     * @param context 上下文
     * @param key 键名
     * @param content 内容
     * @return
     */
    @JvmStatic
    fun putJSONCache(context: Context, key: String?, content: String?) {
        val sp = context.getSharedPreferences(JSON_CACHE, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(key, content)
        editor.apply()
    }

    /**
     * 读取JSON缓存数据
     *
     * @param context 上下文
     * @param key 键名
     * @return
     */
    @JvmStatic
    fun readJSONCache(context: Context, key: String?): String? {
        val sp = context.getSharedPreferences(JSON_CACHE, Context.MODE_PRIVATE)
        return sp.getString(key, null)
    }

    /**
     * 清除指定的信息
     *
     * @param context 上下文
     * @param name 键名
     * @param key     若为null 则删除name下所有的键值
     */
    @JvmStatic
    fun clearPreference(context: Context, name: String?, key: String?) {
        val sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if (key != null) {
            editor.remove(key)
        } else {
            editor.clear()
        }
        editor.apply()
    }
}