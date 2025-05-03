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

/**
 * אדפטר עבור הצגת רשימת טיולים ציבוריים ברכיב RecyclerView.
 * מאפשר גם ביצוע חיפוש ברשימה.
 */
public class RecyclerAdapterLibraryTrip  extends RecyclerView.Adapter<RecyclerAdapterLibraryTrip.ViewHolder>{

    private Context context; // הקשר של המסך שממנו נוצר האדפטר

    private ArrayList<Trip> originalList;  // רשימת כל הטיולים הציבוריים המקוריות (לפני סינון)
    private ArrayList<Trip> filteredList;  // רשימת הטיולים המסוננות שתוצג בפועל ב-RecyclerView

    /**
     * פעולה בונה - מאתחלת את רשימת הפעולות הציבוריות בלבד.
     * @param context הקשר של האקטיביטי או הפרגמנט
     * @param tripsArrayList רשימת כל הטיולים (כולל פרטיים)
     */
    public RecyclerAdapterLibraryTrip(Context context, ArrayList<Trip> tripsArrayList) {
        this.context = context;
        this.originalList = new ArrayList<>();
        for (int i=0; i<tripsArrayList.size(); i++) {
            Trip trip = tripsArrayList.get(i);
            if (trip.getPublicORprivate().equals("isPublic"))
                this.originalList.add(trip);
        }
        this.filteredList = new ArrayList<>(this.originalList);
    }

    /**
     * פעולה שמייצרת ViewHolder עבור פריט טיול.
     * @param parent ה-ViewGroup המכיל את הפריט
     * @param viewType סוג הפריט (לא בשימוש כאן)
     * @return אובייקט ViewHolder חדש
     */
    @NonNull
    @Override
    public RecyclerAdapterLibraryTrip.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_trip_for_lv, parent, false);
        return new ViewHolder(view);
    }

    /**
     * פעולה שמקשרת בין הנתונים של טיול אחד לרכיבי התצוגה.
     * גם מאזינה ללחיצה על הפריט ומעבירה למסך צפייה בפרטי טיול.
     * @param holder ה-ViewHolder שאליו מקשרים את הנתונים
     * @param position מיקום הפריט ברשימה המסוננת
     */
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

    /**
     * מחזירה את מספר הפריטים ברשימה המסוננת.
     * @return מספר הטיולים להצגה
     */
    @Override
    public int getItemCount() {
        if(filteredList == null)
            return 0;
        else
            return filteredList.size();
    }

    /**
     * מסנן את רשימת הטיולים על פי מחרוזת חיפוש.
     * מבצע השוואה מול שדות שונים של הטיול.
     * @param query מחרוזת החיפוש של המשתמש
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filterSearch(String query) {
        filteredList.clear();
        if (query.isEmpty())
            filteredList.addAll(originalList);
        else {
            query = query.toLowerCase(); // ממיר את הכתוב בחיפוש לאותיות קטנות

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
//    public void filters(){
//        //TODO:  כשיהיה לי זמן להוסיף את הסינון לפי קריטריון מסויים
//
//    }

    /**
     * מחלקת ViewHolder פנימית - מחזיקה את רכיבי התצוגה של כל פריט טיול.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView topic; // שפ הטיול
        TextView length; // אורך הטיול בק"מ
        TextView age; // גיל החניכים
        TextView area; // אזור הטיול
        TextView place; // מיקום מדוייק של הטיול
        TextView userName; //שם המשתמש שפרסם את הטיול
        TextView organization; // תנועת הנוער בה נמצא מפרסם הטיול
        ConstraintLayout parentLayout; // הפריסה הכוללת של כל פריט ברשימה
        /**
         * פעולה בונה של ה-ViewHolder, קושרת את הרכיבים מה-XML.
         * @param itemView הפריט הבודד ברשימה
         */
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
