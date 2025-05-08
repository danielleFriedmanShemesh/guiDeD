package com.example.guided.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.guided.Classes.Part;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.R;

import java.util.List;

/**
 * מחלקת Adapter שמטרתה להציג רשימת אובייקטים מסוג {@link Part} בתוך ListView.
 * כל פריט מוצג לפי תבנית XML בשם 'one_part_layout'.
 */
public class PartsListViewAdapter extends ArrayAdapter<Part> {
    private Context context; //ההקשר שבו מופעל האדפטר(ACTIVITY)
    private List<Part> partList; //רשימת מקטעי המסלול (Part) להצגה ב-ListView.

    /**
     * בנאי של המחלקה. מקבל את ההקשר והרשימה להצגה.
     *
     * @param context ההקשר.
     * @param resource מזהה של משאב (לא בשימוש ישיר כאן).
     * @param textViewResourceId מזהה של TextView (לא בשימוש ישיר כאן).
     * @param objects רשימת אובייקטים מסוג Part להצגה.
     */
    public PartsListViewAdapter(
            @NonNull Context context,
            int resource,
            int textViewResourceId,
            @NonNull List<Part> objects) {
        super(
                context,
                resource,
                textViewResourceId,
                objects);
        this.context = context;
        this.partList= objects;
    }

    /**
     * מחזירה View מותאם לכל פריט ברשימה, לפי המיקום שלו.
     *
     * @param position מיקום הפריט ברשימה.
     * @param convertView View לשימוש חוזר (לא מנוצל כאן).
     * @param parent ה-ViewGroup של הרשימה (ListView).
     * @return View המכיל את פרטי האובייקט למיקום הנתון.
     */
    @NonNull
    @Override
    public View getView(
            int position,
            @Nullable View convertView,
            @NonNull ViewGroup parent) {
        // יצירת View חדש מתבנית ה-XML
        LayoutInflater layoutInflater =((Activity)context)
                .getLayoutInflater();
        View view = layoutInflater
                .inflate(
                        R.layout.one_part_layout,
                        parent,
                        false);

        // קישור בין רכיבי ה-XML למשתנים בקוד
        TextView activity = view.findViewById(R.id.activity);
        TextView lengthInMinutes = view.findViewById(R.id.lengthInMinutes);
        TextView lengthInKM = view.findViewById(R.id.lengthInKM);
        TextView description = view.findViewById(R.id.description);
        TextView equipment = view.findViewById(R.id.equipment);
        ImageView picture = view.findViewById(R.id.picture);

        // שליפת אובייקט Part לפי המיקום ברשימה
        Part temp = partList.get(position);

        // עדכון רכיבי ה-View לפי ערכי האובייקט
        activity.setText(temp.getActivityType());
        lengthInMinutes.setText(String.valueOf(temp.getLengthInMinute()));
        lengthInKM.setText(String.valueOf(temp.getLengthInKM()));
        description.setText(temp.getDescription());
        equipment.setText(temp.getEquipment());
        picture.setImageBitmap(
                BitmapHelper
                        .stringToBitmap(
                                temp.getPicture()));

        return view;
    }

}
