package com.example.guided;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMyOperationsAdapter extends RecyclerView.Adapter<RecyclerMyOperationsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Operation> operationArrayList;
    private User user;

    public RecyclerMyOperationsAdapter(Context context, ArrayList<Operation> operations, User user) {
        this.context = context;
        this.user = user;

        this.operationArrayList = new ArrayList<>();;
        for (int i = (operations.size()-1); i>=0; i--) {
            Operation operation = operations.get(i);
            if (operation.getUserName().equals(user.getUserName()))
                operationArrayList.add(operation);
        }
    }

    @NonNull
    @Override
    public RecyclerMyOperationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_1_my_operations, parent, false);
        RecyclerMyOperationsAdapter.ViewHolder viewHolder = new RecyclerMyOperationsAdapter.ViewHolder(view);
        return viewHolder;     }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMyOperationsAdapter.ViewHolder holder, int position) {

        Operation operation = operationArrayList.get(position);
        holder.topic.setText(operation.getNameOfOperation());
        holder.time.setText(operation.getLengthOfOperation()+" דקות ");
        holder.age.setText(operation.getAge());
        holder.goals.setText(operation.getGoals());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_operation.class);
                intent.putExtra("operationKey", operation.getKey());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(operationArrayList == null){
            return 0;
        }
        else {
            return operationArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        CardView parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            topic = itemView.findViewById(R.id.topic1);
            time = itemView.findViewById(R.id.time1);
            age = itemView.findViewById(R.id.ageOp1);
            goals = itemView.findViewById(R.id.goals1);
            parentLayout = itemView.findViewById(R.id.layout_1_my_operations);
        }

        public TextView getAge() {
            return age;
        }

        public TextView getGoals() {
            return goals;
        }

        public CardView getParentLayout() {
            return parentLayout;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getTopic() {
            return topic;
        }
    }
}
