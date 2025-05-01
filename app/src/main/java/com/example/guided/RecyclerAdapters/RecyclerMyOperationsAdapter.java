package com.example.guided.RecyclerAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Activities.Add_operation;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.Classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerMyOperationsAdapter extends RecyclerView.Adapter<RecyclerMyOperationsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Operation> operationArrayList;
    private User user;



    public RecyclerMyOperationsAdapter(Context context, ArrayList<Operation> operations, User user) {
        this.context = context;
        this.user = user;

        this.operationArrayList = new ArrayList<>();
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

    @SuppressLint("SetTextI18n")
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

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                TextView title = dialog.findViewById(R.id.titleDialog);
                Button save = dialog.findViewById(R.id.save);
                Button delete = dialog.findViewById(R.id.delete);

                title.setText("תרצו למחוק את הפעולה?");
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getBindingAdapterPosition();
                        Operation deleteOp = operationArrayList.get(position);
                        String operationId = deleteOp.getKey();

                        FirebaseDatabase.getInstance()
                                .getReference("operations")
                                .child(operationId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // Remove from RecyclerView

                                        operationArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, operationArrayList.size());
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return false;
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
