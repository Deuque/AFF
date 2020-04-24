package com.dcinspirations.aff.models;

public class AdminModel {
    private String A_email,A_pass;

    public AdminModel(String a_email, String a_pass) {
        A_email = a_email;
        A_pass = a_pass;
    }

    public AdminModel() {
    }

    public String getA_email() {
        return A_email;
    }

    public void setA_email(String a_email) {
        A_email = a_email;
    }

    public String getA_pass() {
        return A_pass;
    }

    public void setA_pass(String a_pass) {
        A_pass = a_pass;
    }
}
