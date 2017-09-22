package com.vondear.rxtools.model.scaner;

import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.vondear.rxtools.activity.ActivityScanerCode;


/**
 * Author: Vondear
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

    DecodeThread decodeThread = null;
    ActivityScanerCode activity = null;
    private State state;

    public CaptureActivityHandler(ActivityScanerCode activity) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == com.vondear.rxtools.R.id.auto_focus) {
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, com.vondear.rxtools.R.id.auto_focus);
            }
        } else if (message.what == com.vondear.rxtools.R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == com.vondear.rxtools.R.id.decode_succeeded) {
            state = State.SUCCESS;
            activity.handleDecode((Result) message.obj);// 解析成功，回调
        } else if (message.what == com.vondear.rxtools.R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), com.vondear.rxtools.R.id.decode);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(com.vondear.rxtools.R.id.decode_succeeded);
        removeMessages(com.vondear.rxtools.R.id.decode_failed);
        removeMessages(com.vondear.rxtools.R.id.decode);
        removeMessages(com.vondear.rxtools.R.id.auto_focus);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), com.vondear.rxtools.R.id.decode);
            CameraManager.get().requestAutoFocus(this, com.vondear.rxtools.R.id.auto_focus);
        }
    }

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

}
