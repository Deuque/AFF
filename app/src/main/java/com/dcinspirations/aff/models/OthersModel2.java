package com.dcinspirations.aff.models;

public class OthersModel2 {
    private String address, imgUrl, id;

    public OthersModel2(String address, String id) {
        this.address = address;
        this.id = id;
    }

    public OthersModel2() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
