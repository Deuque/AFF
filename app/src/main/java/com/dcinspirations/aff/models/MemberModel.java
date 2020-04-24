package com.dcinspirations.aff.models;

public class MemberModel {
    private String uid,email,name,category,imgurl,pass;

    public MemberModel(String uid, String email, String name, String category, String imgurl, String pass) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.category = category;
        this.imgurl = imgurl;
        this.pass = pass;
    }

    public MemberModel() {
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
