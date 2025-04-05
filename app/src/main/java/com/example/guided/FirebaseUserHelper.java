package com.example.guided;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUserHelper {
    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    private ArrayList<User> userArrayList;


    public FirebaseUserHelper() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        userArrayList = new ArrayList<>();

    }

    public interface DataStatus {
        void onDataLoaded(ArrayList<User> users);
    }

    public interface UserDataCallback {
        void onUserDataLoaded(User user);
        void onError(String errorMessage);
    }

    public void fetchUsers(FirebaseUserHelper.DataStatus dataStatus) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null) {
                        userArrayList.add(user);
                    }
                }
                // Notify that data is loaded
                dataStatus.onDataLoaded(userArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void fetchUserData(UserDataCallback callback) {

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = userRef.child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        callback.onUserDataLoaded(user);
                    } else {
                        callback.onError("User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
        }

        else if (userRef == null) {
            callback.onError("User is not authenticated");
            return;
        }
    }
}
