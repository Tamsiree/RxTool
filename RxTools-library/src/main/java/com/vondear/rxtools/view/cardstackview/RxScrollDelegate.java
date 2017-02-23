package com.vondear.rxtools.view.cardstackview;

public interface RxScrollDelegate {

    void scrollViewTo(int x, int y);
    void setViewScrollY(int y);
    void setViewScrollX(int x);
    int getViewScrollY();
    int getViewScrollX();

}
