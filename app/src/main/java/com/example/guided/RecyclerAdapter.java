package com.example.guided;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private ArrayList<Metoda> metodaArrayList;
    private Context context;

    public RecyclerAdapter(ArrayList<Metoda> metodaArrayList, Context context) {
        this.metodaArrayList = metodaArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_metoda_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Metoda metoda = metodaArrayList.get(position);
        holder.title.setText(metoda.getTitle());
        holder.length.setText(String.valueOf(metoda.getLength()));
        holder.description.setText(metoda.getDescription());
        holder.equipment.setText(metoda.getEquipment());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(metodaArrayList != null){
                    int id;
                    Metoda metoda = null;
                    //Edit the metoda
                    metoda = metodaArrayList.get(holder.getBindingAdapterPosition());
                    id = metoda.getId();

                    Dialog metodaDialog;
                    metodaDialog = new Dialog(context);
                    metodaDialog.setContentView(R.layout.metoda_layout);
                    metodaDialog.setCancelable(true);

                    EditText metodaLengthET;
                    EditText titleET;
                    EditText descriptionET;
                    EditText equipmentET;
                    Button saveMetodaBTN;

                    titleET = metodaDialog.findViewById(R.id.title);
                    metodaLengthET = metodaDialog.findViewById(R.id.lengthInMinutes);
                    descriptionET = metodaDialog.findViewById(R.id.description);
                    equipmentET = metodaDialog.findViewById(R.id.equipment);
                    saveMetodaBTN = metodaDialog.findViewById(R.id.saveMetoda);

                    titleET.setText(metoda.getTitle());
                    metodaLengthET.setText(String.valueOf(metoda.getLength()));
                    descriptionET.setText(metoda.getDescription());
                    equipmentET.setText(metoda.getEquipment());

                    saveMetodaBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int metodaLengthInt = Integer.parseInt(metodaLengthET.getText().toString());;
                            String titleStr = titleET.getText().toString();
                            String descriptionStr = descriptionET.getText().toString();
                            String equipmentStr = equipmentET.getText().toString();
                            Metoda editMetoda = new Metoda(titleStr, metodaLengthInt, descriptionStr, equipmentStr, id);

                            metodaArrayList.set(id, editMetoda);

                            holder.title.setText(editMetoda.getTitle());
                            holder.length.setText(String.valueOf(editMetoda.getLength()));
                            holder.description.setText(editMetoda.getDescription());
                            holder.equipment.setText(editMetoda.getEquipment());

                            metodaDialog.dismiss();
                        }
                    });
                    metodaDialog.show();
                }

            }
        });
    }

    //מספר השורות שיהיה
    @Override
    public int getItemCount() {
        if(metodaArrayList == null){
            return 0;
        }
        else{
            return metodaArrayList.size();}
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView length;
        TextView description;
        TextView equipment;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            length = itemView.findViewById(R.id.lengthInMinutes);
            description = itemView.findViewById(R.id.description);
            equipment = itemView.findViewById(R.id.equipment);
            parentLayout = itemView.findViewById(R.id.oneMetodaLayout);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getLength() {
            return length;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getEquipment() {
            return equipment;
        }


    }
}
