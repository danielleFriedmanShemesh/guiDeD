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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Activities.Add_trip;
import com.example.guided.R;
import com.example.guided.Classes.Trip;
import com.example.guided.Classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Adapter עבור RecyclerView שמציג את רשימת הטיולים של המשתמש הנוכחי בלבד.
 */
public class RecyclerMyTripsAdapter extends RecyclerView.Adapter<RecyclerMyTripsAdapter.ViewHolder>{

    private Context context; // ההקשר של האקטיביטי/פרגמנט הנוכחי של האפליקציה
    private ArrayList<Trip> tripArrayList; //רשימת טיולים המסוננת כך שתכיל רק טיולים של המשתמש הנוכחי
    private User user; //אובייקט שמכיל את פרטי המשתמש המחובר

    /**
     * בנאי - יוצר את המתאם ומסנן את הטיולים לפי שם המשתמש.
     * @param context ההקשר של האקטיביטי/פרגמנט
     * @param trips רשימת כל הטיולים
     * @param user המשתמש הנוכחי
     */
    public RecyclerMyTripsAdapter(
            ArrayList<Trip> trips,
            Context context,
            User user) {
        this.context = context;
        this.user = user;
        this.tripArrayList = new ArrayList<>();

        for (int i = (trips.size()-1); i>=0; i--) {
            Trip trip = trips.get(i);
            if (trip.getUserName().equals(user.getUserName()))
                tripArrayList.add(trip);
        }
    }

    /**
     * יוצר ViewHolder חדש - טוען את קובץ העיצוב של השורה
     */
    @NonNull
    @Override
    public RecyclerMyTripsAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(
                R.layout.layout_1_my_trips,
                parent,
                false);
        return new ViewHolder(view);
    }

    /**
     * קושר את הנתונים של הטיול לממשק, כולל לחיצה ופעולות מחיקה
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull RecyclerMyTripsAdapter.ViewHolder holder,
            int position) {

        Trip trip = tripArrayList.get(position);
        holder.topic.setText(trip.getNameOfTrip());
        holder.length.setText(trip.getLengthInKm()+" ק''מ ");
        holder.age.setText(trip.getAge());
        holder.area.setText(trip.getArea());
        holder.place.setText(trip.getPlace());

        // לחיצה רגילה - מעבר לעריכה
        holder.parentLayout.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_trip.class);
                intent.putExtra("tripKey", trip.getKey());
                context.startActivity(intent);

            }
        });

        // לחיצה ארוכה - פתיחת דיאלוג מחיקה
        holder.parentLayout.setOnLongClickListener(
                new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete_layout);
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                TextView title = dialog.findViewById(R.id.titleDialog);
                Button save = dialog.findViewById(R.id.save);
                Button delete = dialog.findViewById(R.id.delete);

                title.setText("תרצו למחוק את הטיול?");
                delete.setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getBindingAdapterPosition();
                        Trip deleteTr = tripArrayList.get(position);
                        String tripId = deleteTr.getKey();

                        FirebaseDatabase.getInstance()
                                .getReference("trips")
                                .child(tripId)
                                .removeValue()
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // מוחק מה RecyclerView
                                        tripArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(
                                                position,
                                                tripArrayList.size());
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                save.setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return false;
            }
        });
    }

    /**
     * מחזיר את מספר הפריטים ברשימה
     */
    @Override
    public int getItemCount() {
        if(tripArrayList == null)
            return 0;
        else
            return tripArrayList.size();
    }

    /**
     * מחלקה פנימית - מייצגת שורה ברשימת הטיולים
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic; // שם הטיול
        TextView length; // אורך הטיול בק"מ
        TextView age; // גיל החניכים
        TextView area; // אזור הטיול
        TextView place; // מיקום מדוייק ש הטיול
        CardView parentLayout; // פריסת השורה

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
