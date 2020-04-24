package com.dcinspirations.aff.models;

public class NewsModel {
    private String ntitle, nbody, nlink,date,category,nkey;

    public NewsModel(String ntitle, String nbody, String nlink, String date, String category) {
        this.ntitle = ntitle;
        this.nbody = nbody;
        this.nlink = nlink;
        this.date = date;
        this.category = category;
    }

    public NewsModel() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNtitle() {
        return ntitle;
    }

    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }

    public String getNbody() {
        return nbody;
    }

    public void setNbody(String nbody) {
        this.nbody = nbody;
    }

    public String getNlink() {
        return nlink;
    }

    public void setNlink(String nlink) {
        this.nlink = nlink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNkey() {
        return nkey;
    }

    public void setNkey(String nkey) {
        this.nkey = nkey;
    }
}
