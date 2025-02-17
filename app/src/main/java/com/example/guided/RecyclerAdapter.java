package com.example.guided;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.title.setText(metodaArrayList.get(position).getTitle());
        holder.length.setText(String.valueOf(metodaArrayList.get(position).getLength()));
        holder.description.setText(metodaArrayList.get(position).getDescription());
        holder.equipment.setText(metodaArrayList.get(position).getEquipment());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            length = itemView.findViewById(R.id.lengthInMinutes);
            description = itemView.findViewById(R.id.description);
            equipment = itemView.findViewById(R.id.equipment);
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
