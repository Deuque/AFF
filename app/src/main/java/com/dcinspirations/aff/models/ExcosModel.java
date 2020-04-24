package com.dcinspirations.aff.models;

public class ExcosModel {
    private String imgUrl,name,position,key;

    public ExcosModel(String imgUrl, String name, String position) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.position = position;
    }

    public ExcosModel() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
