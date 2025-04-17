package com.example.guided;

public class Part {
    private String activityType;
    private int lengthInMinute;
    private double lengthInKM;
    private String description;
    private String equipment;
    private String picture;
    private int id;

    public Part(String activityType, int lengthInMinute, double lengthInKM, String description, String equipment, String picture, int id) {
        this.activityType = activityType;
        this.lengthInMinute = lengthInMinute;
        this.lengthInKM = lengthInKM;
        this.description = description;
        this.equipment = equipment;
        this.picture = picture;
        this.id = id;
    }
    public Part(){}

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getLengthInMinute() {
        return lengthInMinute;
    }

    public void setLengthInMinute(int lengthInMinute) {
        this.lengthInMinute = lengthInMinute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLengthInKM() {
        return lengthInKM;
    }

    public void setLengthInKM(int lengthInKM) {
        this.lengthInKM = lengthInKM;
    }


}
