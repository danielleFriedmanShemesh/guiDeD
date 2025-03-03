package com.example.guided;

import java.util.ArrayList;

public class Operation {
    private String nameOfOperation;
    private String userName;
    private String age;
    private String privateORpublic;
    private int lengthOfOperation;
    private String organization;
    private ArrayList<Metoda> metodotArr = new ArrayList<>();
    private String goals;
    private String equipment;

    public Operation(String nameOfOperation,
                     String age,
                     String privateORpublic,
                     int lengthOfOperation,
                     String goals,
                     String equipment,
                     ArrayList<Metoda> metodotArr) {
        this.nameOfOperation = nameOfOperation;
        this.privateORpublic = privateORpublic;
        this.age = age;
        this.lengthOfOperation = lengthOfOperation;
        this.goals = goals;
        this.equipment = equipment;
        this.metodotArr = metodotArr;

        //לשלוף מהמסד נתונים של המשתמשים את שם המשתמש ואת התנועה שלו
    }

    public String getNameOfOperation() {
        return nameOfOperation;
    }

    public void setNameOfOperation(String nameOfOperation) {
        this.nameOfOperation = nameOfOperation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPrivateORpublic() {
        return privateORpublic;
    }

    public void setPrivateORpublic(String privateORpublic) {
        this.privateORpublic = privateORpublic;
    }

    public int getLengthOfOperation() {
        return lengthOfOperation;
    }

    public void setLengthOfOperation(int lengthOfOperation) {
        this.lengthOfOperation = lengthOfOperation;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public ArrayList<Metoda> getMetodotArr() {
        return metodotArr;
    }

    public void setMetodotArr(ArrayList<Metoda> metodotArr) {
        this.metodotArr = metodotArr;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
