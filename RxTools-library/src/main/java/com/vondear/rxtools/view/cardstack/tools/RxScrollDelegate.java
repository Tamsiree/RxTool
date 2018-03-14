package com.vondear.rxtools.view.cardstack.tools;

/**
 * @author vondear
 */
public interface RxScrollDelegate {

    void scrollViewTo(int x, int y);

    int getViewScrollY();

    void setViewScrollY(int y);

    int getViewScrollX();

    void setViewScrollX(int x);

}
