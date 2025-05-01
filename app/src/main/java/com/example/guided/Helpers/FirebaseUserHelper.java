package com.example.guided.Helpers;

import androidx.annotation.NonNull;

import com.example.guided.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * מחלקה המנהלת פעולות של שליפת נתוני משתמשים ממסד הנתונים Firebase Realtime Database.
 * תומכת גם בשליפה של כל המשתמשים וגם בשליפה של המשתמש המחובר.
 */
public class FirebaseUserHelper {
    private DatabaseReference userRef; // הפניה למסד הנתונים בענף "users"
    private  FirebaseUser currentUser; //המשתמש הנוכחי שמחובר דרך Firebase Authentication
    private ArrayList<User> userArrayList; //רשימה של כל המשתמשים שנשלפו מהמסד

    /**
     * פעולה בונה שמאתחלת את FirebaseAuth, את המשתמש המחובר הנוכחי,
     * את ההפניה למסד הנתונים ואת הרשימה של המשתמשים.
     */
    public FirebaseUserHelper() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        userRef = FirebaseDatabase
                .getInstance()
                .getReference(
                        "users");
        userArrayList = new ArrayList<>();

    }

    /**
     * ממשק להאזנה כאשר הנתונים נטענים ממסד הנתונים.
     */
    public interface DataStatus {
        /**
         * מופעל כאשר הנתונים נטענו בהצלחה.
         * @param users רשימת המשתמשים שהתקבלה מהמסד.
         */
        void onDataLoaded(ArrayList<User> users);
    }

    /**
     * ממשק לשליפת נתוני משתמש בודד.
     */
    public interface UserDataCallback {
        /**
         * מופעל כאשר נתוני המשתמש נטענו בהצלחה.
         * @param user אובייקט המשתמש שהתקבל.
         */
        void onUserDataLoaded(User user);

        /**
         * מופעל כאשר מתרחשת שגיאה בעת שליפת הנתונים.
         * @param errorMessage הודעת שגיאה.
         */
        void onError(String errorMessage);
    }


    /**
     * שולף את כל המשתמשים מהענף "users" במסד הנתונים.
     * מעדכן את הרשימה ומפעיל את הקולבק כאשר הנתונים מוכנים.
     *
     * @param dataStatus ממשק עם פעולה שמופעלת כשהשליפה מצליחה.
     */
    public void fetchUsers(
            FirebaseUserHelper
                    .DataStatus dataStatus) {
        userRef.addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(
                    @NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null) {
                        userArrayList.add(user);
                    }
                }
                // מודיע שהנתונים נטענו
                dataStatus.onDataLoaded(userArrayList);

            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError error) {
                // אין טיפול בשגיאה כאן
            }
        });
    }

    /**
     * שולף את נתוני המשתמש שמחובר כרגע, לפי ה־UID שלו.
     * אם הנתונים קיימים – מעביר אותם לקולבק, אחרת מחזיר שגיאה.
     *
     * @param callback ממשק לקבלת תוצאה של שליפת נתוני המשתמש.
     */
    public void fetchUserData(UserDataCallback callback) {

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = userRef.child(userId);

            userRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                @Override
                public void onDataChange(
                        @NonNull DataSnapshot snapshot) {
                    User user = snapshot
                            .getValue(User.class);
                    if (user != null) {
                        callback.onUserDataLoaded(user);
                    } else {
                        callback.onError(
                                "נתוני המשתמש לא נמצאו");
                    }
                }

                @Override
                public void onCancelled(
                        @NonNull DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
        }

        else if (userRef == null) {
            callback.onError(
                    "משתמש לא מחובר");
        }
    }
}
