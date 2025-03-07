package com.example.guided;

import java.util.ArrayList;

public class Trip {
    private String userName;
    private String organization;
    private String nameOfTrip;
    private String age;
    private String publicORprivate;
    private int lengthInKm;
    private int lengthInMinutes;
    private String place;
    private String area;
    private String goals;
    private String equipments;
    private ArrayList<Part> partsArr = new ArrayList<>();

    public Trip(String nameOfTrip,
                String age,
                String privateORpublic,
                int lengthOfTripInKM,
                int lengthOfTripInMinutes,
                String goals,
                String equipment,
                String area,
                String place,
                ArrayList<Part> partsArr,
                String userName,
                String organization) {
        this.nameOfTrip = nameOfTrip;
        this.publicORprivate = privateORpublic;
        this.age = age;
        this.lengthInKm = lengthOfTripInKM;
        this.lengthInMinutes = lengthOfTripInMinutes;
        this.goals = goals;
        this.equipments = equipment;
        this.area = area;
        this.place = place;
        this.partsArr = partsArr;
        this.userName = userName;
        this.organization = organization;
    }

    public Trip(){
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getNameOfTrip() {
        return nameOfTrip;
    }

    public void setNameOfTrip(String nameOfTrip) {
        this.nameOfTrip = nameOfTrip;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPublicORprivate() {
        return publicORprivate;
    }

    public void setPublicORprivate(String publicORprivate) {
        this.publicORprivate = publicORprivate;
    }

    public int getLengthInKm() {
        return lengthInKm;
    }

    public void setLengthInKm(int lengthInKm) {
        this.lengthInKm = lengthInKm;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getEquipments() {
        return equipments;
    }

    public void setEquipments(String equipments) {
        this.equipments = equipments;
    }

    public ArrayList<Part> getPartsArr() {
        return partsArr;
    }

    public void setPartsArr(ArrayList<Part> partsArr) {
        this.partsArr = partsArr;
    }
}
