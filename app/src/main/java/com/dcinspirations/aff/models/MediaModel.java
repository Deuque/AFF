package com.dcinspirations.aff.models;

public class MediaModel {
    private String imgUrl,artist,song,type,download,key;

    public MediaModel(String imgUrl, String artist, String song, String type) {
        this.imgUrl = imgUrl;
        this.artist = artist;
        this.song = song;
        this.type = type;
    }

    public MediaModel() {
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
