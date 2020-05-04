package com.dcinspirations.aff.models;

public class OthersModel {

    private String mvtext, cptext,interesttext,membertext;

    public OthersModel(String mvtext, String cptext, String interesttext, String membertext) {
        this.mvtext = mvtext;
        this.cptext = cptext;
        this.interesttext = interesttext;
        this.membertext = membertext;
    }

    public OthersModel() {
    }

    public String getMvtext() {
        return mvtext;
    }

    public void setMvtext(String mvtext) {
        this.mvtext = mvtext;
    }

    public String getCptext() {
        return cptext;
    }

    public void setCptext(String cptext) {
        this.cptext = cptext;
    }

    public String getInteresttext() {
        return interesttext;
    }

    public void setInteresttext(String interesttext) {
        this.interesttext = interesttext;
    }

    public String getMembertext() {
        return membertext;
    }

    public void setMembertext(String membertext) {
        this.membertext = membertext;
    }
}
