package com.example.guided;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseOperationHelper {
    private DatabaseReference myRef;
    private ArrayList<Operation> operationArrayList;

    public FireBaseOperationHelper() {
        myRef = FirebaseDatabase.getInstance().getReference("operations");
        operationArrayList = new ArrayList<>();
    }

    public interface DataStatus {
        void onDataLoaded(ArrayList<Operation> operations);
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
    public ArrayList<Operation> getOperationArrayList(){
        return operationArrayList;
    }
}
