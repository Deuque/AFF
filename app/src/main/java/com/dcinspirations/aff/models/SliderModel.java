package com.dcinspirations.aff.models;

public class SliderModel {
    private String title,category;
    private String imgid;
    private String imgUrl;


    public SliderModel(String title, String category, String imgUrl) {
        this.title = title;
        this.category = category;
        this.imgUrl = imgUrl;
    }

    public SliderModel() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }
}
