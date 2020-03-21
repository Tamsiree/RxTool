package com.tamsiree.rxkit

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 *
 * @author tamsiree
 * @date 2016/12/21
 */
object RxClipboardTool {
    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    @JvmStatic
    fun copyText(context: Context, text: CharSequence?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text))
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    @JvmStatic
    fun getText(context: Context): CharSequence? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).coerceToText(context)
        } else null
    }

    /**
     * 复制uri到剪贴板
     *
     * @param uri uri
     */
    @JvmStatic
    fun copyUri(context: Context, uri: Uri?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newUri(context.contentResolver, "uri", uri))
    }

    /**
     * 获取剪贴板的uri
     *
     * @return 剪贴板的uri
     */
    @JvmStatic
    fun getUri(context: Context): Uri? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).uri
        } else null
    }

    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    @JvmStatic
    fun copyIntent(context: Context, intent: Intent?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent))
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    @JvmStatic
    fun getIntent(context: Context): Intent? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).intent
        } else null
    }
}