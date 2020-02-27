package com.tamsiree.rxdemo.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxui.activity.ActivityBase;

public class ActivityOnCrash extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_crash);


        Button crashMainThreadButton = findViewById(R.id.button_crash_main_thread);
        Button crashBgThreadButton = findViewById(R.id.button_crash_bg_thread);
        Button crashWithDelayButton = findViewById(R.id.button_crash_with_delay);

        crashMainThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("I'm a cool exception and I crashed the main thread!");
            }
        });

        crashBgThreadButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak") //For demo purposes we don't care about leaks
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        throw new RuntimeException("I'm also cool, and I crashed the background thread!");
                    }
                }.execute();
            }
        });

        crashWithDelayButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak") //For demo purposes we don't care about leaks
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            //meh
                        }
                        throw new RuntimeException("I am a not so cool exception, and I am delayed, so you can check if the app crashes when in background!");
                    }
                }.execute();
            }
        });
    }
}
