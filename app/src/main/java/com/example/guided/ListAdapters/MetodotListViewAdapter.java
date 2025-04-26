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

public class MetodotListViewAdapter extends ArrayAdapter<Metoda> {
    private Context context;
    private List<Metoda> metodaList;


    public MetodotListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Metoda> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.metodaList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater layoutInflater =((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_metoda_layout, parent, false);

        TextView title = view.findViewById(R.id.title);
        TextView length = view.findViewById(R.id.lengthInMinutes);
        TextView description = view.findViewById(R.id.description);
        TextView equipment = view.findViewById(R.id.equipment);

        Metoda temp = metodaList.get(position);

        title.setText(temp.getTitle());
        length.setText(String.valueOf(temp.getLength()));
        description.setText(temp.getDescription());
        equipment.setText(temp.getEquipment());

        return view;
    }
}
