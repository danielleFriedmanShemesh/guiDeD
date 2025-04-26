package com.example.guided.RecyclerAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.R;
import com.example.guided.Classes.Trip;
import com.example.guided.Activities.View_trip;

import java.util.ArrayList;

public class RecyclerAdapterLibraryTrip  extends RecyclerView.Adapter<RecyclerAdapterLibraryTrip.ViewHolder>{

    private Context context;

    private ArrayList<Trip> originalList;  // Original Data
    private ArrayList<Trip> filteredList;  // Filtered Data

    public RecyclerAdapterLibraryTrip(Context context, ArrayList<Trip> tripsArrayList) {
        this.context = context;
        this.originalList = new ArrayList<>();
        for (int i=0; i<tripsArrayList.size(); i++) {
            Trip trip = tripsArrayList.get(i);
            if (trip.getPublicORprivate().equals("isPublic")) {
                this.originalList.add(trip);

            }
        }
        this.filteredList = new ArrayList<>(this.originalList);
    }

    @NonNull
    @Override
    public RecyclerAdapterLibraryTrip.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_trip_for_lv, parent, false);
        RecyclerAdapterLibraryTrip.ViewHolder viewHolder = new RecyclerAdapterLibraryTrip.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLibraryTrip.ViewHolder holder, int position) {

        Trip trip = filteredList.get(position);
        holder.topic.setText(trip.getNameOfTrip());
        holder.length.setText(trip.getLengthInKm()+" ק''מ ");
        holder.age.setText(trip.getAge());
        holder.area.setText(trip.getArea());
        holder.place.setText(trip.getPlace());
        holder.userName.setText(trip.getUserName());
        holder.organization.setText(trip.getOrganization());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, View_trip.class);
                intent.putExtra("tripKey", trip.getKey());
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
            return filteredList.size();
        }
    }
    public void filterSearch(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            query = query.toLowerCase(); // It converts the search query (user input) to lowercase letters.


            for (Trip trip : originalList) {
                if (trip.getNameOfTrip().toLowerCase().contains(query) ||
                        String.valueOf(trip.getLengthInKm()).contains(query) ||
                        trip.getArea().toLowerCase().contains(query) ||
                        trip.getPlace().toLowerCase().contains(query) ||
                        trip.getOrganization().toLowerCase().contains(query) ||
                        trip.getUserName().toLowerCase().contains(query) ||
                        String.valueOf(trip.getAge()).contains(query)) {
                    filteredList.add(trip);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void filters(){
        //TODO:  כשיהיה לי זמן להוסיף את הסינון לפי קריטריון מסויים

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView topic;
        TextView length;
        TextView age;
        TextView area;
        TextView place;
        TextView userName;
        TextView organization;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            topic = itemView.findViewById(R.id.topic);
            length = itemView.findViewById(R.id.length);
            age = itemView.findViewById(R.id.age);
            area = itemView.findViewById(R.id.areaOfTR);
            place = itemView.findViewById(R.id.place);
            userName = itemView.findViewById(R.id.userName);
            organization = itemView.findViewById(R.id.organization);
            parentLayout = itemView.findViewById(R.id.one_trip_for_lv);
        }

        public TextView getAge() {
            return age;
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getTopic() {
            return topic;
        }

        public TextView getPlace() {
            return place;
        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }

        public TextView getOrganization() {
            return organization;
        }

        public TextView getArea() {
            return area;
        }

        public TextView getLength() {
            return length;
        }
    }
}
