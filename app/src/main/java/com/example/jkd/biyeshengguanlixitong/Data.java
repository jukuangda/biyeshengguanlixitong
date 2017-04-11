package com.example.jkd.biyeshengguanlixitong;

import android.app.Application;
/**
 * Created by jkd on 2016/3/27.
 */
public class Data extends Application{
    private String PHONE;
//    private String zczt;
    public String GetPHONE(){return this.PHONE;}
    public void SetPHONE(String phone){this.PHONE = phone;}
//    public String GetZCZT(){return this.zczt;}
//    public void SetZCZT(String zczt){this.zczt = zczt;}
    @Override
    public void onCreate(){
        PHONE = null;
//        zczt="0";
        super.onCreate();
    }
}
