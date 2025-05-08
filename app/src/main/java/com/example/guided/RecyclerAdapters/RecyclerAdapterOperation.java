package com.example.guided.RecyclerAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Classes.Metoda;
import com.example.guided.R;

import java.util.ArrayList;

public class RecyclerAdapterOperation extends RecyclerView.Adapter<RecyclerAdapterOperation.ViewHolder>{

    private ArrayList<Metoda> metodaArrayList; //רשימת המתודות של הפעולה. כל אובייקט מסוג Metoda מייצג מתודה מהפעולה
    private Context context;//הקשר (Context) של האפליקציה, משמש לגישה למשאבים וליצירת דיאלוגים.
    private OnMetodaListChangedListener listener; // מאזין לשינויים ברשימת המתודות, מאפשר לעדכן את הממשק בהתאם לשינויים

    /**
     * מגדיר את המאזין לשינויים ברשימת המתודות.
     *
     * @param listener המאזין לשינויים.
     */
    public void setOnMetodaListChangedListener(
            OnMetodaListChangedListener listener) {
        this.listener = listener;
    }

    public interface OnMetodaListChangedListener {
        void onMetodaListChanged(ArrayList<Metoda> metodot);
    }

    /**
     * בנאי המחלקה.
     *
     * @param metodaArrayList רשימת המתודות של הפעולה.
     * @param context        הקשר של האפליקציה.
     */
    public RecyclerAdapterOperation(
            ArrayList<Metoda> metodaArrayList,
            Context context) {
        this.metodaArrayList = metodaArrayList;
        this.context = context;
    }

    /**
     * יוצר ViewHolder חדש עבור פריט ברשימה.
     *
     * @param parent   ההורה של ה-View.
     * @param viewType סוג ה-View.
     * @return ViewHolder חדש.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_metoda_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * קושר את הנתונים של מתודה מסוים ל-ViewHolder.
     *
     * @param holder   ה-ViewHolder שאליו יקשרו הנתונים.
     * @param position המיקום של הפריט ברשימה.
     */
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {
        Metoda metoda = metodaArrayList.get(position);
        holder.title.setText(metoda.getTitle());
        holder.length.setText(String.valueOf(metoda.getLength()));
        holder.description.setText(metoda.getDescription());
        holder.equipment.setText(metoda.getEquipment());
        holder.parentLayout.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(metodaArrayList != null){
                    int id;
                    Metoda metoda;
                    //Edit the metoda
                    metoda = metodaArrayList.get(
                            holder.getBindingAdapterPosition());
                    id = holder.getBindingAdapterPosition();

                    Dialog metodaDialog;
                    metodaDialog = new Dialog(context);
                    metodaDialog.setContentView(R.layout.metoda_layout);
                    metodaDialog.setCancelable(true);

                    EditText metodaLengthET;
                    EditText titleET;
                    EditText descriptionET;
                    EditText equipmentET;
                    Button saveMetodaBTN;

                    titleET = metodaDialog.findViewById(R.id.title);
                    metodaLengthET = metodaDialog.findViewById(R.id.lengthInMinutes);
                    descriptionET = metodaDialog.findViewById(R.id.description);
                    equipmentET = metodaDialog.findViewById(R.id.equipment);
                    saveMetodaBTN = metodaDialog.findViewById(R.id.saveMetoda);

                    titleET.setText(metoda.getTitle());
                    metodaLengthET.setText(String.valueOf(metoda.getLength()));
                    descriptionET.setText(metoda.getDescription());
                    equipmentET.setText(metoda.getEquipment());

                    saveMetodaBTN.setOnClickListener(
                            new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int metodaLengthInt = Integer.parseInt(
                                    metodaLengthET.getText().toString());;
                            String titleStr = titleET.getText().toString();
                            String descriptionStr = descriptionET.getText().toString();
                            String equipmentStr = equipmentET.getText().toString();
                            Metoda editMetoda = new Metoda(
                                    titleStr,
                                    metodaLengthInt,
                                    descriptionStr,
                                    equipmentStr,
                                    id);

                            metodaArrayList.set(id, editMetoda);

                            holder.title.setText(editMetoda.getTitle());
                            holder.length.setText(
                                    String.valueOf(
                                            editMetoda.getLength()));
                            holder.description.setText(editMetoda.getDescription());
                            holder.equipment.setText(editMetoda.getEquipment());

                            if (listener != null)
                                listener.onMetodaListChanged(metodaArrayList);

                            metodaDialog.dismiss();
                        }
                    });
                    metodaDialog.show();
                }
            }
        });
    }

    /**
     * מחזיר את מספר הפריטים ברשימה.
     *
     * @return מספר הפריטים.
     */
    @Override
    public int getItemCount() {
        if(metodaArrayList == null)
            return 0;
        else
            return metodaArrayList.size();
    }

    /**
     * מחזיק את ה-View עבור פריט ברשימה.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView length;
        TextView description;
        TextView equipment;
        ConstraintLayout parentLayout;

        /**
         * בנאי המחלקה.
         *
         * @param itemView ה-View של הפריט.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            length = itemView.findViewById(R.id.lengthInMinutes);
            description = itemView.findViewById(R.id.description);
            equipment = itemView.findViewById(R.id.equipment);
            parentLayout = itemView.findViewById(R.id.oneMetodaLayout);
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
