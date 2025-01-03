package com.example.guided;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class User {
    private String userName;
    private String password;
    private String email;
    private String nickName;
    private Bitmap profileImage;
    private int dayBirth;
    private int monthBirth;
    private int yearBirth;
    private String organization;


    public User(String userName, String password, String email){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.nickName="";
        this.yearBirth=0;
        this.dayBirth=0;
        this.monthBirth=0;
        this.organization="";
        this.profileImage=null;

    }

    public User(String userName, String password, String email, String nickName, String organization, int dayBirth, int monthBirth, int yearBirth, Bitmap image){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.nickName=nickName;
        this.organization=organization;
        this.yearBirth=yearBirth;
        this.dayBirth=dayBirth;
        this.monthBirth=monthBirth;
        this.profileImage= image;

    }

    public User(){

    }

    public byte[] bitmapToBlob(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Compress the bitmap into the byte array (you can specify a format and quality)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();}

    public int getYearBirth() {
        return yearBirth;
    }

    public void setYearBirth(int yearBirth) {
        this.yearBirth = yearBirth;
    }

    public int getMonthBirth() {
        return monthBirth;
    }

    public void setMonthBirth(int monthBirth) {
        this.monthBirth = monthBirth;
    }

    public int getDayBirth() {
        return dayBirth;
    }

    public void setDayBirth(int dayBirth) {
        this.dayBirth = dayBirth;
    }

    public Bitmap getProfileImage(){
        return this.profileImage;
    }

    public void setProfileImage(Bitmap profileImage){
        this.profileImage=profileImage;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getNickName(){
        return this.nickName;
    }

    public void setNickName(String nickName){
        this.nickName=nickName;
    }

    public String getOrganization(){
        return this.organization;
    }

    public void setOrganization(String organization){
        this.organization=organization;
    }










}
