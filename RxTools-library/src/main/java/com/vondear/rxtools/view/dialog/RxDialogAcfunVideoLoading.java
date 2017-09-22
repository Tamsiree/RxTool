package com.vondear.rxtools.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vondear.rxtools.R;

import java.util.Random;

public class RxDialogAcfunVideoLoading extends RxDialog {
    private ProgressBar loading_progressBar;
    private TextView tv_reminder;
    private String[] loadingText = {"对的，坚持；错的，放弃！", "你若安好，便是晴天。", "走得太快，灵魂都跟不上了。", "生气是拿别人的错误惩罚自己。", "让未来到来，让过去过去。", "每一种创伤，都是一种成熟。"};

    public RxDialogAcfunVideoLoading(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        View dialogView = LayoutInflater.from(context).inflate(
                R.layout.dialog_loading_progress_acfun_video, null);
        loading_progressBar = (ProgressBar) dialogView.findViewById(R.id.loading_progressBar);
        Random random = new Random();
        int number = Math.abs(random.nextInt() % loadingText.length);
        tv_reminder = (TextView) dialogView.findViewById(R.id.tv_reminder);
        tv_reminder.setText(loadingText[number]);
        setContentView(dialogView);
        getLayoutParams().gravity = Gravity.CENTER;
    }

    public ProgressBar getLoadingBar() {
        return loading_progressBar;
    }

    public TextView getReminderView() {
        return tv_reminder;
    }

}
