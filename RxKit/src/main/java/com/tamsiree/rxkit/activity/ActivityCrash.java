package com.tamsiree.rxkit.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.tamsiree.rxkit.R;
import com.tamsiree.rxkit.RxAppTool;
import com.tamsiree.rxkit.RxLogTool;
import com.tamsiree.rxkit.crash.RxCrashConfig;
import com.tamsiree.rxkit.crash.RxCrashTool;

import java.io.File;

public class ActivityCrash extends FragmentActivity {

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is needed to avoid a crash if the developer has not specified
        //an app-level theme that extends Theme.AppCompat
        TypedArray a = obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        }
        a.recycle();

        setContentView(R.layout.activity_crash);

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        Button restartButton = findViewById(R.id.crash_error_activity_restart_button);
        Button closeButton = findViewById(R.id.crash_error_activity_close_button);
        TextView tvCrashTool = findViewById(R.id.rx_crash_tool);

        final RxCrashConfig config = RxCrashTool.getConfigFromIntent(getIntent());

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.crash_error_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxCrashTool.restartApplication(ActivityCrash.this, config);
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxCrashTool.closeApplication(ActivityCrash.this, config);
                }
            });
        } else {
            closeButton.setVisibility(View.GONE);
        }
        String message = RxCrashTool.getAllErrorDetailsFromIntent(ActivityCrash.this, getIntent());
        File file = RxLogTool.e(message);

        String appName = RxAppTool.getAppName(this);
        tvCrashTool.setText(appName);

        TextView locateButton = findViewById(R.id.crash_error_locate_more_info_button);
        locateButton.setText(locateButton.getText() + "\n\n" + file.getAbsolutePath() + "\n");

        Button moreInfoButton = findViewById(R.id.crash_error_activity_more_info_button);

        if (config.isShowErrorDetails()) {
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //We retrieve all the error data and show it


                    AlertDialog dialog = new AlertDialog.Builder(ActivityCrash.this)
                            .setTitle(R.string.crash_error_details_title)
                            .setMessage(message)
                            .setPositiveButton(R.string.crash_error_details_close, null)
                            .setNeutralButton(R.string.crash_error_details_copy,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                        }
                                    })
                            .show();
                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
                    }
                }
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }

        Integer defaultErrorActivityDrawableId = config.getErrorDrawable();
        ImageView errorImageView = findViewById(R.id.crash_error_activity_image);

        if (defaultErrorActivityDrawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), defaultErrorActivityDrawableId, getTheme()));
        }
    }

    private void copyErrorToClipboard() {
        String errorInformation = RxCrashTool.getAllErrorDetailsFromIntent(ActivityCrash.this, getIntent());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.crash_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ActivityCrash.this, R.string.crash_error_details_copied, Toast.LENGTH_SHORT).show();
        }
    }
}
