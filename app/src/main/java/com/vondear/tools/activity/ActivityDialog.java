package com.vondear.tools.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;

import com.vondear.tools.R;
import com.vondear.vontools.VonUtils;
import com.vondear.vontools.view.DialogEditTextSureCancle;
import com.vondear.vontools.view.DialogSure;
import com.vondear.vontools.view.DialogSureCancle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDialog extends AppCompatActivity {

    @BindView(R.id.button_DialogSure)
    Button buttonDialogSure;
    @BindView(R.id.button_DialogSureCancle)
    Button buttonDialogSureCancle;
    @BindView(R.id.button_DialogEditTextSureCancle)
    Button buttonDialogEditTextSureCancle;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        context = this;
    }

    @OnClick({R.id.button_DialogSure, R.id.button_DialogSureCancle, R.id.button_DialogEditTextSureCancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_DialogSure:
                final DialogSure dialogSure = new DialogSure(context);//提示弹窗
                dialogSure.getTv_content().setMovementMethod(ScrollingMovementMethod.getInstance());
                dialogSure.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSure.cancel();
                    }
                });
                dialogSure.show();
                break;
            case R.id.button_DialogSureCancle:
                final DialogSureCancle dialogSureCancle = new DialogSureCancle(context);//提示弹窗
                dialogSureCancle.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSureCancle.cancel();
                    }
                });
                dialogSureCancle.getTv_cancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSureCancle.cancel();
                    }
                });
                dialogSureCancle.show();
                break;
            case R.id.button_DialogEditTextSureCancle:
                final DialogEditTextSureCancle dialogEditTextSureCancle = new DialogEditTextSureCancle(context);//提示弹窗
                dialogEditTextSureCancle.getTv_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEditTextSureCancle.cancel();
                    }
                });
                dialogEditTextSureCancle.getTv_cancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEditTextSureCancle.cancel();
                    }
                });
                dialogEditTextSureCancle.show();
                break;
        }
    }
}
