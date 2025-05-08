package com.example.guided.Classes;

public class Metoda {
    private String title;
    private int length;
    private String description;
    private String equipment;
    private int id;

    public Metoda(String title , int length , String description , String equipment, int id){
        this.title = title;
        this.length = length;
        this.description = description;
        this.equipment = equipment;
        this.id = id;
    }

    public Metoda() {
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
