package com.example.guided;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String nickName;
    private Bitmap profileImage;

    private Date birthday;
    private String organization;


    public User(String userName, String password, String email){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.nickName="";
        this.birthday=new Date();
        this.organization="";
        this.profileImage=null;

    }

    public User(String userName, String password, String email, String nickName, String organization, Date birthday, Bitmap image){
        //int dayBirth, int monthBirth, int yearBirth
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.nickName=nickName;
        this.organization=organization;
        /*this.yearBirth=yearBirth;
        this.dayBirth=dayBirth;
        this.monthBirth=monthBirth;*/
        this.birthday=birthday;
        this.profileImage= image;

    }

    public User(){

    }

    public byte[] bitmapToBlob(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Compress the bitmap into the byte array (you can specify a format and quality)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();}



    public Date getBirthday(){
        return this.birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday=birthday;
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
