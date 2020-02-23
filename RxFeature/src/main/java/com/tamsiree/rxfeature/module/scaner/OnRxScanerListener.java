package com.tamsiree.rxfeature.module.scaner;


import com.google.zxing.Result;

/**
 * @author Tamsiree
 * @date 2017/9/22
 */

public interface OnRxScanerListener {
    void onSuccess(String type, Result result);

    void onFail(String type, String message);
}
