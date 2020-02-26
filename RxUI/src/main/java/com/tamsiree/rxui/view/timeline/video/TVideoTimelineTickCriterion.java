
package com.tamsiree.rxui.view.timeline.video;

public class TVideoTimelineTickCriterion {

    private int viewLength;

    private int totalSecondsInOneScreen;

    private int keyTickInSecond;

    private int minTickInSecond;

    private String dataPattern;

    public int getViewLength() {
        return viewLength;
    }

    public void setViewLength(int viewLength) {
        this.viewLength = viewLength;
    }

    public int getTotalSecondsInOneScreen() {
        return totalSecondsInOneScreen;
    }

    public void setTotalSecondsInOneScreen(int totalSecondsInOneScreen) {
        this.totalSecondsInOneScreen = totalSecondsInOneScreen;
    }

    public int getKeyTickInSecond() {
        return keyTickInSecond;
    }

    public void setKeyTickInSecond(int keyTickInSecond) {
        this.keyTickInSecond = keyTickInSecond;
    }

    public int getMinTickInSecond() {
        return minTickInSecond;
    }

    public void setMinTickInSecond(int minTickInSecond) {
        this.minTickInSecond = minTickInSecond;
    }

    public String getDataPattern() {
        return dataPattern;
    }

    public void setDataPattern(String dataPattern) {
        this.dataPattern = dataPattern;
    }
}
