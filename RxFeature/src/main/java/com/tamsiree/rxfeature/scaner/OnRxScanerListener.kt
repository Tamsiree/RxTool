package com.tamsiree.rxfeature.scaner

import com.google.zxing.Result

/**
 * @author Tamsiree
 * @date 2017/9/22
 */
interface OnRxScanerListener {
    fun onSuccess(type: String?, result: Result?)
    fun onFail(type: String?, message: String?)
}