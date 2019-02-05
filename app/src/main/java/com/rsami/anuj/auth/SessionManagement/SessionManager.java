package com.rsami.anuj.auth.SessionManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.rsami.anuj.auth.MainActivity;
import com.rsami.anuj.auth.model.RSA;
import com.rsami.anuj.auth.model.aceKeys;
import com.google.gson.Gson;

import java.util.HashMap;


public class SessionManager{
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    public static RSA rsa ;
    static Context _context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "LogedInUser";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_Name = "name";
    public static final String KEY_Email = "email";
    public static final String KEY_Pno = "PhoneNo";
    public static final String KEY_Dob = "DOB";
    public static final String KEY_Details = "details";
    public static final String KEY_MemberShipNo = "membershipno";
    public static final String KEY_Pwd = "pwd";
    public static final String KEY_Status = "status";
    public static final String KEY_Type = "type";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static String getDob() {
        String dob = pref.getString(KEY_Dob, null);
        return dob;
    }

    public static void setDob(String name) {
        editor.putString(KEY_Dob, name);
        editor.commit();
    }

    public void setKeys(String pb,String pr){
        rsa = new RSA();
    }
    public static String getPno() {
        return  pref.getString(KEY_Pno, null);
    }

    public static void setPno(String name) {
        editor.putString(KEY_Pno, name);
        editor.commit();
    }



    public static String getMemberShipNo(){
        return pref.getString(KEY_MemberShipNo, null);
    }

    public static void setMemberShipNo(String name) {
        editor.putString(KEY_MemberShipNo, name);
        editor.commit();
    }

    public static String getType() {
        return pref.getString(KEY_Type, null);
    }

    public static void setType(String name) {
        editor.putString(KEY_Type, name);
        editor.commit();
    }

    public static String getStatus() {
        return pref.getString(KEY_Status, null);
    }

    public static void setStatus(String name) {
        editor.putString(KEY_Status, name);
        editor.commit();
    }

    public static String getName() {
        return pref.getString(KEY_Name, null);
    }

    public static void setName(String name) {
        editor.putString(KEY_Name, name);
        editor.commit();
    }

    public static String getEmail() {
        return pref.getString(KEY_Email, null);
    }

    public static void setEmail(String email) {
        editor.putString(KEY_Email, email);
        editor.commit();
    }


    public static String getPwd() {
        return pref.getString(KEY_Pwd, null);
    }

    public static void setPwd(String pwd) {
        editor.putString(KEY_Pwd,pwd);
        editor.commit();
    }


    public static String getDetails() {
        return pref.getString(KEY_Details, null);
    }

    public static void setDetails(String details) {
        editor.putString(KEY_Details, details);
        editor.commit();
    }


    public static void createLoginSession(String pno, String dob, String MemberShipNo, String pwd, String email, String name, String status, String Type, String details) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_Name, name);
        editor.putString(KEY_Email, email);
        editor.putString(KEY_Pno, pno);
        editor.putString(KEY_Dob, dob);
        editor.putString(KEY_Details, details);
        editor.putString(KEY_MemberShipNo, MemberShipNo);
        editor.putString(KEY_Pwd, pwd);
        editor.putString(KEY_Status, status);
        editor.putString(KEY_Type, Type);
        editor.commit();
    }


    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);

    }

    public static boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


}