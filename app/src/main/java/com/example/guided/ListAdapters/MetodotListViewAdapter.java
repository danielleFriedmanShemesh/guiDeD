package com.example.guided.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.guided.Classes.Metoda;
import com.example.guided.R;

import java.util.List;

/**
 * מחלקת Adapter שמטרתה להציג רשימת אובייקטים מסוג {@link Metoda} בתוך ListView.
 * כל פריט מוצג לפי תבנית XML בשם 'one_metoda_layout'.
 */
public class MetodotListViewAdapter extends ArrayAdapter<Metoda> {
    private Context context; //ההקשר שבו מופעל האדפטר(ACTIVITY)
    private List<Metoda> metodaList; // רשימת מטודות בפעולה (Metoda) להצגה ב-ListView.


    /**
     * בנאי של המחלקה. מקבל את ההקשר והרשימה להצגה.
     *
     * @param context ההקשר.
     * @param resource מזהה של משאב (לא בשימוש ישיר כאן).
     * @param textViewResourceId מזהה של TextView (לא בשימוש ישיר כאן).
     * @param objects רשימת אובייקטים מסוג Metoda להצגה.
     */
    public MetodotListViewAdapter(
            @NonNull Context context,
            int resource,
            int textViewResourceId,
            @NonNull List<Metoda> objects) {
        super(
                context,
                resource,
                textViewResourceId,
                objects);
        this.context = context;
        this.metodaList = objects;
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
                        R.layout.one_metoda_layout,
                        parent,
                        false);

        // קישור בין רכיבי ה-XML למשתנים בקוד
        TextView title = view.findViewById(R.id.title);
        TextView length = view.findViewById(R.id.lengthInMinutes);
        TextView description = view.findViewById(R.id.description);
        TextView equipment = view.findViewById(R.id.equipment);

        // שליפת אובייקט Metoda לפי המיקום ברשימה
        Metoda temp = metodaList.get(position);

        // עדכון רכיבי ה-View לפי ערכי האובייקט
        title.setText(temp.getTitle());
        length.setText(String.valueOf(temp.getLength()));
        description.setText(temp.getDescription());
        equipment.setText(temp.getEquipment());

        return view;
    }
}
