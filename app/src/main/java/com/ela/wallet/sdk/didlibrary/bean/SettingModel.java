package com.ela.wallet.sdk.didlibrary.bean;

public class SettingModel {

    private int img;
    private String title;
    private String subtitle;

    public SettingModel() {
    }

    public SettingModel(String title) {
        this.title = title;
    }

    public SettingModel(int img, String title, String subtitle) {
        this.img = img;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
