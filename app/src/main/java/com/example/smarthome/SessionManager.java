package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE=0;
    private  static final String PREF_NAME="LOGIN";
    private  static final String LOGIN="IS_LOGIN";
    public  static final String CIN="cin";
    public  static final String USERNAME="USERNAME";
    public  static final String EMAIL="EMAIL";
    public  static final String FULL_NAME="fullname";
    public  static final String PHONE="phone";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor =sharedPreferences.edit();
    }
    public void information(String fullname,String phone,String cin){
        editor.putString(FULL_NAME,fullname);
        editor.putString(PHONE,phone);
        editor.putString(CIN,cin);
    }
    public void createSession(String username, String email){
        editor.putBoolean(LOGIN,true);
        editor.putString(USERNAME,username);
        editor.putString(EMAIL,email);
        editor.apply();
    }
    public void createSession2(String username, String email,String CIN){
        editor.putBoolean(LOGIN,true);
        editor.putString(USERNAME,username);
        editor.putString(EMAIL,email);
        editor.putString(CIN,CIN);
        editor.apply();
    }
    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }
    public void checkLogin(){
        if(!this.isLoggin()){
            Intent i =new Intent(context,login.class);
            context.startActivity(i);
            ((Principal)context).finish();
        }
    }
    public HashMap<String, String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(USERNAME,sharedPreferences.getString(USERNAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        return  user;
    }
    public void logout(){
        editor.clear();
        editor.commit();

    }
}
