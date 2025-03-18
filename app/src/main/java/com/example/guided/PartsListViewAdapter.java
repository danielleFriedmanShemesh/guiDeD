package com.example.guided;

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

import java.util.List;

public class PartsListViewAdapter extends ArrayAdapter<Part> {

    private Context context;
    private List<Part> partList;

    public PartsListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Part> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.partList= objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater layoutInflater =((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_part_layout, parent, false);

        TextView activity = view.findViewById(R.id.activity);
        TextView lengthInMinutes = view.findViewById(R.id.lengthInMinutes);
        TextView lengthInKM = view.findViewById(R.id.lengthInKM);
        TextView description = view.findViewById(R.id.description);
        TextView equipment = view.findViewById(R.id.equipment);
        ImageView picture = view.findViewById(R.id.picture);

        Part temp = partList.get(position);

        activity.setText(temp.getActivityType());
        lengthInMinutes.setText(String.valueOf(temp.getLengthInMinute()));
        lengthInKM.setText(String.valueOf(temp.getLengthInKM()));
        description.setText(temp.getDescription());
        equipment.setText(temp.getEquipment());
        picture.setImageBitmap(BitmapHelper.stringToBitmap(temp.getPicture()));

        return view;
    }
}
