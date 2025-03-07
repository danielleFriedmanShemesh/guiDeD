package com.example.guided;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterLibraryTrip  extends RecyclerView.Adapter<RecyclerAdapterLibraryTrip.ViewHolder>{

    private Context context;
    private ArrayList<Trip> tripsArrayList;

    public RecyclerAdapterLibraryTrip(Context context, ArrayList<Trip> tripsArrayList) {
        this.context = context;
        this.tripsArrayList = new ArrayList<>();
        for (int i=0; i<tripsArrayList.size(); i++) {
            Trip trip = tripsArrayList.get(i);
            if (trip.getPublicORprivate().equals("isPublic")) {
                this.tripsArrayList.add(trip);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterLibraryTrip.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_trip_for_lv, parent, false);
        RecyclerAdapterLibraryTrip.ViewHolder viewHolder = new RecyclerAdapterLibraryTrip.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLibraryTrip.ViewHolder holder, int position) {

        Trip trip = tripsArrayList.get(position);
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
//                Intent intent = new Intent(this, view_operation.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tripsArrayList == null){
            return 0;
        }
        else {
            return tripsArrayList.size();
        }
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
            area = itemView.findViewById(R.id.area);
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
