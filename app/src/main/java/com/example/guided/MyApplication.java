package com.example.guided;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {
    private static ArrayList<Metoda> metodot = new ArrayList<Metoda>();
    private static int nextId = 0;
    public MyApplication(){}

    public static ArrayList<Metoda> getMetodot() {
        return metodot;
    }

    public static void setMetodot(ArrayList<Metoda> metodot) {
        MyApplication.metodot = metodot;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        MyApplication.nextId = nextId;
    }
}
