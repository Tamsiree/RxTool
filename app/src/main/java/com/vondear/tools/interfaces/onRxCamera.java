package com.vondear.tools.interfaces;

import java.io.File;

/**
 * Created by Vondear on 2017/8/9.
 */

public interface onRxCamera {
    void onBefore();

    void onSuccessCompress(File filePhoto);

    void onSuccessExif(File filePhoto);
}