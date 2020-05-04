package com.dcinspirations.aff.models;

public class MemberModel2 {
    private String stage_name,place_birth,lga_origin,s_origin,nationality,lga_reside,edu_level,mar_status,ref,imgUrl;

    public MemberModel2(String stage_name, String place_birth, String lga_origin, String s_origin, String nationality, String lga_reside, String edu_level, String mar_status, String ref) {
        this.stage_name = stage_name;
        this.place_birth = place_birth;
        this.lga_origin = lga_origin;
        this.s_origin = s_origin;
        this.nationality = nationality;
        this.lga_reside = lga_reside;
        this.edu_level = edu_level;
        this.mar_status = mar_status;
        this.ref = ref;
    }

    public MemberModel2() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }



    public String getPlace_birth() {
        return place_birth;
    }

    public void setPlace_birth(String place_birth) {
        this.place_birth = place_birth;
    }

    public String getLga_origin() {
        return lga_origin;
    }

    public void setLga_origin(String lga_origin) {
        this.lga_origin = lga_origin;
    }

    public String getS_origin() {
        return s_origin;
    }

    public void setS_origin(String s_origin) {
        this.s_origin = s_origin;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLga_reside() {
        return lga_reside;
    }

    public void setLga_reside(String lga_reside) {
        this.lga_reside = lga_reside;
    }

    public String getEdu_level() {
        return edu_level;
    }

    public void setEdu_level(String edu_level) {
        this.edu_level = edu_level;
    }

    public String getMar_status() {
        return mar_status;
    }

    public void setMar_status(String mar_status) {
        this.mar_status = mar_status;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
