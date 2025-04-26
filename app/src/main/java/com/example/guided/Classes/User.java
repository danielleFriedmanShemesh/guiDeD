package com.example.guided.Classes;


import androidx.annotation.NonNull;


import java.util.Date;
import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String nickName;
    private String profileImage;
    private Date birthday;
    private String organization;


    public User(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.nickName = "";
        this.birthday = new Date();
        this.organization = "";
        this.profileImage = "";

    }

    public User(String userName, String password, String email, String nickName, String organization, Date birthday, String image){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.nickName=nickName;
        this.organization=organization;
        this.birthday=birthday;
        this.profileImage= image;

    }

    public User(){
        this.userName="";
        this.password="";
        this.email="";
        this.nickName="";
        this.birthday=new Date();
        this.organization="";
        this.profileImage="";

    }





    public Date getBirthday(){
        return this.birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday=birthday;
    }

    public String getProfileImage(){
        return this.profileImage;
    }

    public void setProfileImage(String profileImage){
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


    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", profileImage=" + profileImage +
                ", birthday=" + birthday +
                ", organization='" + organization + '\'' +
                '}';
    }


}
