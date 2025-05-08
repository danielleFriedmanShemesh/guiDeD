package com.example.guided.Helpers;

import androidx.annotation.NonNull;

import com.example.guided.Classes.Operation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * מחלקה זו מטפלת בשליפת פעולות ממסד הנתונים Firebase Realtime Database.
 * היא מאפשרת שליפה של כל הפעולות, פעולות לפי שם משתמש, ושליפת פעולה אחת לפי מזהה.
 */
public class FireBaseOperationHelper {
    private DatabaseReference myRef; //הפניה למסד הנתונים בענף "operations"
    private ArrayList<Operation> operationArrayList; //רשימת הפעולות שנשלפו ממסד הנתונים
    private Operation operation; // פעולה בודדת שמיועדת להצגה

    /**
     * פעולה בונה של המחלקה. מאתחלת את ההפניה למסד הנתונים, רשימת הפעולות, ואובייקט הפעולה.
     */
    public FireBaseOperationHelper() {
        myRef = FirebaseDatabase
                .getInstance()
                .getReference(
                        "operations");
        operationArrayList = new ArrayList<>();
        operation = new Operation();
    }

    /**
     * ממשק המשמש לקבלת תוצאה של רשימת פעולות.
     */
    public interface DataStatus {
        /**
         * מופעל כאשר הנתונים נטענו בהצלחה.
         * @param operations רשימת הפעולות שנשלפו
         */
        void onDataLoaded(ArrayList<Operation> operations);
    }

    /**
     * ממשק המשמש לקבלת תוצאה של פעולה בודדת.
     */
    public interface DataStatusM {
        /**
         * מופעל כאשר פעולה בודדת נטענת בהצלחה.
         * @param operation הפעולה שנשלפה
         */
        void onDataLoaded(Operation operation);
    }

    /**
     * טוענת את כל הפעולות של משתמש מסוים לפי שם המשתמש.
     * @param dataStatus הממשק דרכו מחזירים את הפעולות שנטענו
     * @param userName שם המשתמש שאליו שייכים הפעולות
     */
    public void fetchMyOperations(DataStatus dataStatus, String userName){
        myRef.orderByChild("userName")
                .equalTo(userName)
                .addValueEventListener(
                        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                operationArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Operation operation = data.getValue(Operation.class);
                    if (operation != null)
                        operationArrayList.add(operation);
                }
                // החזרת התוצאה אם קיימת
                if (!operationArrayList.isEmpty())
                    dataStatus.onDataLoaded(operationArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * טוענת את כל הפעולות מהמסד ללא סינון.
     * @param dataStatus הממשק דרכו מחזירים את הפעולות שנטענו
     */
    public void fetchOperations(DataStatus dataStatus) {
        myRef.addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                operationArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Operation operation = data.getValue(Operation.class);
                    if (operation != null)
                        operationArrayList.add(operation);
                }
                // החזרת התוצאה אם קיימת
                dataStatus.onDataLoaded(operationArrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * טוענת פעולה אחת לפי מזהה.
     * @param dataStatusM הממשק דרכו מחזירים את הפעולה
     * @param id מזהה הפעולה (המפתח ב-Firebase)
     */
    public void fetchOneOperation(
            DataStatusM dataStatusM,
            String id){
        myRef.addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(id))
                        operation = dataSnapshot.getValue(Operation.class);
                }
               dataStatusM.onDataLoaded(operation);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
