package com.example.guided.Helpers;

import androidx.annotation.NonNull;

import com.example.guided.Classes.Operation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseOperationHelper {
    private DatabaseReference myRef;
    private ArrayList<Operation> operationArrayList;
    private Operation operation;

    public FireBaseOperationHelper() {
        myRef = FirebaseDatabase.getInstance().getReference("operations");
        operationArrayList = new ArrayList<>();
        operation = new Operation();
    }

    public interface DataStatus {
        void onDataLoaded(ArrayList<Operation> operations);
    }

    public interface DataStatusM {
        void onDataLoaded(Operation operation);
    }



    public void fetchOperations(DataStatus dataStatus) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                operationArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Operation operation = data.getValue(Operation.class);
                    if (operation != null) {
                        operationArrayList.add(operation);
                    }
                }
                // Notify that data is loaded
                dataStatus.onDataLoaded(operationArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void fetchOneOperation(DataStatusM dataStatusM, String id){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(id)) {
                        operation = dataSnapshot.getValue(Operation.class);
                    }
                }
               dataStatusM.onDataLoaded(operation);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public ArrayList<Operation> getOperationArrayList(){
        return operationArrayList;
    }

    public Operation getOperation() {
        return operation;
    }
}
