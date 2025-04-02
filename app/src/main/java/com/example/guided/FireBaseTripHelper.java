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
    private Trip showTrip;

    public FireBaseTripHelper() {
        myRef = FirebaseDatabase.getInstance().getReference("trips");
        tripsArrayList = new ArrayList<>();
        showTrip = new Trip();
    }
    public interface DataStatus {
        void onDataLoaded(ArrayList<Trip> trips);
    }

    public interface DataStatusT {
        void onDataLoaded(Trip trip);
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
                if (!tripsArrayList.isEmpty())
                    dataStatus.onDataLoaded(tripsArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void fetchOneTrip(FireBaseTripHelper.DataStatusT dataStatusT, String id){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(id)) {
                        showTrip = dataSnapshot.getValue(Trip.class);

                    }

                }
                dataStatusT.onDataLoaded(showTrip);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public Trip getTrip() {
        return showTrip;
    }

    public ArrayList<Trip> getTripsArrayList() {
        return tripsArrayList;
    }
}
