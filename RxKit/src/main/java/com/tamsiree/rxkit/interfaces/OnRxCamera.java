package com.tamsiree.rxkit.interfaces;

import java.io.File;

/**
 *
 * @author Tamsiree
 * @date 2017/8/9
 */

public interface OnRxCamera {

    void onBefore();

    void onSuccessCompress(File filePhoto);

    void onSuccessExif(File filePhoto);
}