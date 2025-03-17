package com.example.guided;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapterLibraryOperation extends RecyclerView.Adapter<RecyclerAdapterLibraryOperation.ViewHolder>{
    private Context context;
    private ArrayList<Operation> originalList;  // Original Data
    private ArrayList<Operation> filteredList;  // Filtered Data

    public RecyclerAdapterLibraryOperation(Context context, ArrayList<Operation> operationArrayList) {
        this.context = context;
        this.originalList = new ArrayList<>();
        for (int i=0; i<operationArrayList.size(); i++) {
            Operation operation = operationArrayList.get(i);
            if (operation.getPrivateORpublic().equals("isPublic")) {
                this.originalList.add(operation);
            }
        }
        this.filteredList = new ArrayList<>(this.originalList);
    }

    @NonNull
    @Override
    public RecyclerAdapterLibraryOperation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_operation_for_lv, parent, false);
        RecyclerAdapterLibraryOperation.ViewHolder viewHolder = new RecyclerAdapterLibraryOperation.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLibraryOperation.ViewHolder holder, int position)
    {
        Operation operation = filteredList.get(position);
        holder.topic.setText(operation.getNameOfOperation());
        holder.time.setText(operation.getLengthOfOperation()+" דקות ");
        holder.age.setText(operation.getAge());
        holder.goals.setText(operation.getGoals());
        holder.userName.setText(operation.getUserName());
        holder.organization.setText(operation.getOrganization());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, View_operation.class);
                intent.putExtra("operationKey", operation.getKey());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        if(filteredList == null){
            return 0;
        }
        else {
            int opsSize = filteredList.size();
            return opsSize;
        }
    }

    public void filterSearch(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            query = query.toLowerCase(); // It converts the search query (user input) to lowercase letters.


            for (Operation operation : originalList) {
                if (operation.getNameOfOperation().toLowerCase().contains(query) ||
                        String.valueOf(operation.getLengthOfOperation()).contains(query) ||
                        operation.getGoals().toLowerCase().contains(query) ||
                        operation.getOrganization().toLowerCase().contains(query) ||
                        operation.getUserName().toLowerCase().contains(query) ||
                        String.valueOf(operation.getAge()).contains(query)) {
                    filteredList.add(operation);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        TextView userName;
        TextView organization;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            time = itemView.findViewById(R.id.time);
            age = itemView.findViewById(R.id.age);
            goals = itemView.findViewById(R.id.goals);
            userName = itemView.findViewById(R.id.writerUserName);
            organization = itemView.findViewById(R.id.organization);
            parentLayout = itemView.findViewById(R.id.one_operation_for_lv);


        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }

        public TextView getOrganization() {
            return organization;
        }

        public TextView getTopic() {
            return topic;
        }

        public TextView getAge() {
            return age;
        }

        public TextView getGoals() {
            return goals;
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getTime() {
            return time;
        }

    }
}
