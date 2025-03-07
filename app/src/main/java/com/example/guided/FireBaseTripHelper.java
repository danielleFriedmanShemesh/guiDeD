package com.example.guided;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseTripHelper {
    private DatabaseReference myRef;
    private ArrayList<Trip> tripsArrayList;

    public FireBaseTripHelper() {
        myRef = FirebaseDatabase.getInstance().getReference("trips");
        tripsArrayList = new ArrayList<>();
    }
    public interface DataStatus {
        void onDataLoaded(ArrayList<Trip> trips);
    }

    public void fetchTrips(FireBaseTripHelper.DataStatus dataStatus) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripsArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Trip trip = data.getValue(Trip.class);
                    if (trip != null) {
                        tripsArrayList.add(trip);
                    }
                }
                // Notify that data is loaded
                dataStatus.onDataLoaded(tripsArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public ArrayList<Trip> getTripsArrayList() {
        return tripsArrayList;
    }
}
