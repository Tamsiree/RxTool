package com.tamsiree.rxui.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import com.tamsiree.rxui.R
import java.util.*

/**
 * @author tamsiree
 */
class RxDialogAcfunVideoLoading(context: Context?) : RxDialog(context!!) {
    val loadingBar: ProgressBar
    val reminderView: TextView
    private val loadingText = arrayOf("对的，坚持；错的，放弃！", "你若安好，便是晴天。", "走得太快，灵魂都跟不上了。", "生气是拿别人的错误惩罚自己。", "让未来到来，让过去过去。", "每一种创伤，都是一种成熟。")

    init {
        val dialogView = LayoutInflater.from(context).inflate(
                R.layout.dialog_loading_progress_acfun_video, null)
        loadingBar = dialogView.findViewById(R.id.loading_progressBar)
        val random = Random()
        val number = Math.abs(random.nextInt() % loadingText.size)
        reminderView = dialogView.findViewById(R.id.tv_reminder)
        reminderView.text = loadingText[number]
        setContentView(dialogView)
        layoutParams!!.gravity = Gravity.CENTER
    }
}