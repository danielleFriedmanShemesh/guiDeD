package com.example.guided;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMyTripsAdapter extends RecyclerView.Adapter<RecyclerMyTripsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Trip> tripArrayList;
    private User user;


    public RecyclerMyTripsAdapter(ArrayList<Trip> trips, Context context, User user) {
        this.context = context;
        this.user = user;
        this.tripArrayList = new ArrayList<>();

        for (int i = (trips.size()-1); i>=0; i--) {
            Trip trip = trips.get(i);
            if (trip.getUserName().equals(user.getUserName()))
                tripArrayList.add(trip);
        }
    }

    @NonNull
    @Override
    public RecyclerMyTripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_1_my_trips, parent, false);
        RecyclerMyTripsAdapter.ViewHolder viewHolder = new RecyclerMyTripsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMyTripsAdapter.ViewHolder holder, int position) {

        Trip trip = tripArrayList.get(position);
        holder.topic.setText(trip.getNameOfTrip());
        holder.length.setText(trip.getLengthInKm()+" ק''מ ");
        holder.age.setText(trip.getAge());
        holder.area.setText(trip.getArea());
        holder.place.setText(trip.getPlace());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_trip.class);
                intent.putExtra("tripKey", trip.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tripArrayList == null){
            return 0;
        }
        else {
            return tripArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic;
        TextView length;
        TextView age;
        TextView area;
        TextView place;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.title1);
            length = itemView.findViewById(R.id.length1);
            age = itemView.findViewById(R.id.age1);
            area = itemView.findViewById(R.id.area1);
            place = itemView.findViewById(R.id.place1);
            parentLayout = itemView.findViewById(R.id.layout_1_my_trips);
        }

        public TextView getAge() {
            return age;
        }

        public TextView getArea() {
            return area;
        }

        public TextView getLength() {
            return length;
        }

        public CardView getParentLayout() {
            return parentLayout;
        }

        public TextView getPlace() {
            return place;
        }

        public TextView getTopic() {
            return topic;
        }
    }
}
