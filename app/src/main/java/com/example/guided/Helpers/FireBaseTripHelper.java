package com.example.guided.Helpers;

import androidx.annotation.NonNull;

import com.example.guided.Classes.Trip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * מחלקה זו מטפלת בשליפת טיולים ממסד הנתונים Firebase Realtime Database.
 * היא מאפשרת שליפה של כל הטיולים, טיולים לפי שם משתמש, ושליפת טיול אחד לפי מזהה.
 */
public class FireBaseTripHelper {
    private DatabaseReference myRef; //הפניה למסד הנתונים בענף "trips"
    private ArrayList<Trip> tripsArrayList; //רשימת הטיולים שנשלפו ממסד הנתונים
    private Trip showTrip; // טיול בודד שמיועד להצגה

    /**
     * פעולה בונה של המחלקה. מאתחלת את ההפניה למסד הנתונים, רשימת הטיולים, ואובייקט הטיול.
     */
    public FireBaseTripHelper() {
        myRef = FirebaseDatabase
                .getInstance()
                .getReference(
                        "trips");
        tripsArrayList = new ArrayList<>();
        showTrip = new Trip();
    }

    /**
     * ממשק המשמש לקבלת תוצאה של רשימת טיולים.
     */
    public interface DataStatus {
        /**
         * מופעל כאשר הנתונים נטענו בהצלחה.
         * @param trips רשימת הטיולים שנשלפו
         */
        void onDataLoaded(ArrayList<Trip> trips);
    }

    /**
     * ממשק המשמש לקבלת תוצאה של טיול בודד.
     */
    public interface DataStatusT {
        /**
         * מופעל כאשר טיול בודד נטען בהצלחה.
         * @param trip הטיול שנשלף
         */
        void onDataLoaded(Trip trip);
    }

    /**
     * טוענת את כל הטיולים של משתמש מסוים לפי שם המשתמש.
     * @param dataStatus הממשק דרכו מחזירים את הטיולים שנטענו
     * @param userName שם המשתמש שאליו שייכים הטיולים
     */
    public void fetchMyTrips(
            FireBaseTripHelper.DataStatus dataStatus,
            String userName){
        myRef.orderByChild("userName")
                .equalTo(userName)
                .addValueEventListener(
                        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripsArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Trip trip = data.getValue(Trip.class);
                    if (trip != null)
                        tripsArrayList.add(trip);
                }
                // החזרת התוצאה אם קיימת
                if (!tripsArrayList.isEmpty())
                    dataStatus.onDataLoaded(tripsArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * טוענת את כל הטיולים מהמסד ללא סינון.
     * @param dataStatus הממשק דרכו מחזירים את הטיולים שנטענו
     */
    public void fetchTrips(
            FireBaseTripHelper.DataStatus dataStatus) {
        myRef.addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripsArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Trip trip = data.getValue(Trip.class);
                    if (trip != null)
                        tripsArrayList.add(trip);
                }
                // החזרת התוצאה אם קיימת
                if (!tripsArrayList.isEmpty())
                    dataStatus.onDataLoaded(tripsArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * טוענת טיול אחד לפי מזהה.
     * @param dataStatusT הממשק דרכו מחזירים את הטיול
     * @param id מזהה הטיול (המפתח ב-Firebase)
     */
    public void fetchOneTrip(
            FireBaseTripHelper.DataStatusT dataStatusT,
            String id){
        myRef.addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(id))
                        showTrip = dataSnapshot.getValue(Trip.class);
                }
                dataStatusT.onDataLoaded(showTrip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
