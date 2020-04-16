package com.tamsiree.rxkit.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.tamsiree.rxkit.R
import com.tamsiree.rxkit.RxAppTool
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxkit.crash.TCrashTool

class ActivityCrash : FragmentActivity() {
    @SuppressLint("PrivateResource", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //This is needed to avoid a crash if the developer has not specified
        //an app-level theme that extends Theme.AppCompat
        val a = obtainStyledAttributes(R.styleable.AppCompatTheme)
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar)
        }
        a.recycle()
        setContentView(R.layout.activity_crash)

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        val restartButton = findViewById<Button>(R.id.crash_error_activity_restart_button)
        val closeButton = findViewById<Button>(R.id.crash_error_activity_close_button)
        val tvCrashTool = findViewById<TextView>(R.id.rx_crash_tool)
        val config = TCrashTool.getConfigFromIntent(intent)
        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish()
            return
        }
        if (config.isShowRestartButton() && config.restartActivityClass != null) {
            restartButton.setText(R.string.crash_error_restart_app)
            restartButton.setOnClickListener { TCrashTool.restartApplication(this@ActivityCrash, config) }
            closeButton.setOnClickListener { TCrashTool.closeApplication(this@ActivityCrash, config) }
        } else {
            closeButton.visibility = View.GONE
        }
        val message = TCrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val file = TLog.e(message)
        val appName = RxAppTool.getAppName(this)
        tvCrashTool.text = appName
        val locateButton = findViewById<TextView>(R.id.crash_error_locate_more_info_button)
        locateButton.text = "${locateButton.text}\n\n${file.absolutePath}\n"
        val moreInfoButton = findViewById<Button>(R.id.crash_error_activity_more_info_button)
        if (config.isShowErrorDetails()) {
            moreInfoButton.setOnClickListener { //We retrieve all the error data and show it
                val dialog = AlertDialog.Builder(this@ActivityCrash)
                        .setTitle(R.string.crash_error_details_title)
                        .setMessage(message)
                        .setPositiveButton(R.string.crash_error_details_close, null)
                        .setNeutralButton(R.string.crash_error_details_copy
                        ) { dialog, which -> copyErrorToClipboard() }
                        .show()
                val textView = dialog.findViewById<TextView>(android.R.id.message)
                textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size))
            }
        } else {
            moreInfoButton.visibility = View.GONE
        }
        val defaultErrorActivityDrawableId = config.errorDrawable
        val errorImageView = findViewById<ImageView>(R.id.crash_error_activity_image)
        if (defaultErrorActivityDrawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, defaultErrorActivityDrawableId, theme))
        }
    }

    private fun copyErrorToClipboard() {
        val errorInformation = TCrashTool.getAllErrorDetailsFromIntent(this@ActivityCrash, intent)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        //Are there any devices without clipboard...?
        val clip = ClipData.newPlainText(getString(R.string.crash_error_details_clipboard_label), errorInformation)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this@ActivityCrash, R.string.crash_error_details_copied, Toast.LENGTH_SHORT).show()
    }
}