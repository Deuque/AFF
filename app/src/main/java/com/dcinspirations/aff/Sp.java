package com.dcinspirations.aff;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Sp {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor spe;
    private Boolean isLoggedIn = false;

    public Sp(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences("default", 0);
        spe = sharedPreferences.edit();
    }

    public static void setLoginType(int state){
        spe.putInt("loginType",state);
        spe.commit();
    }
    public static int getLoginType(){
        int li = sharedPreferences.getInt("loginType",0);
        return li;
    }

    public static void setUid(String uid){
        spe.putString("userId",uid);
        spe.commit();
    }
    public static String getUid(){
        String li = sharedPreferences.getString("userId","");
        return li;
    }


//    public ArrayList<CheckoutModel> getCheckOutList(){
//        ArrayList<CheckoutModel> checkoutlist = new ArrayList<>();
//        String colisttext = sharedPreferences.getString("coliststring","");
//        if(!colisttext.isEmpty()){
//            Type gsontype = new TypeToken<List<CheckoutModel>>() {
//            }.getType();
//            checkoutlist = new Gson().fromJson(colisttext, gsontype);
//        }
//        return checkoutlist;
//
//    }
//
//    public void setCheckOutList(ArrayList<CheckoutModel> cl){
//        String checkoutAsString = new Gson().toJson(cl);
//        spe.putString("coliststring",checkoutAsString);
//        spe.commit();
//
//    }
//    public void clearCheckOutList(){
//        ArrayList<CheckoutModel> colist = getCheckOutList();
//        colist.clear();
//        String checkoutAsString = new Gson().toJson(colist);
//        spe.putString("coliststring",checkoutAsString);
//        spe.commit();
//    }


}
