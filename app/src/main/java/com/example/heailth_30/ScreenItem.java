package com.example.heailth_30;

public class ScreenItem {

    String title, context;
    int ScreenImg;

    public ScreenItem(String title, String context, int screenImg) {
        this.title = title;
        this.context = context;
        ScreenImg = screenImg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public int getScreenImg() {
        return ScreenImg;
    }
}
