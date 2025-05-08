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

import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.Activities.View_operation;

import java.util.ArrayList;

/**
 * אדפטר עבור הצגת רשימת פעולות ציבוריות ברכיב RecyclerView.
 * מאפשר גם ביצוע חיפוש ברשימה.
 */
public class RecyclerAdapterLibraryOperation extends RecyclerView.Adapter<RecyclerAdapterLibraryOperation.ViewHolder>{
    private Context context; // הקשר של המסך שממנו נוצר האדפטר
    private ArrayList<Operation> originalList;  // רשימת כל הפעולות הציבוריות המקוריות (לפני סינון)
    private ArrayList<Operation> filteredList;  // רשימת הפעולות המסוננות שתוצג בפועל ב-RecyclerView

    /**
     * פעולה בונה - מאתחלת את רשימת הפעולות הציבוריות בלבד.
     * @param context הקשר של האקטיביטי או הפרגמנט
     * @param operationArrayList רשימת כל הפעולות (כולל פרטיות)
     */
    public RecyclerAdapterLibraryOperation(Context context, ArrayList<Operation> operationArrayList) {
        this.context = context;
        this.originalList = new ArrayList<>();
        for (int i=0; i<operationArrayList.size(); i++) {
            Operation operation = operationArrayList.get(i);
            if (operation.getPrivateORpublic().equals("isPublic"))
                this.originalList.add(operation);
        }
        this.filteredList = new ArrayList<>(this.originalList);
    }

    /**
     * פעולה שמייצרת ViewHolder עבור פריט פעולה.
     * @param parent ה-ViewGroup המכיל את הפריט
     * @param viewType סוג הפריט (לא בשימוש כאן)
     * @return אובייקט ViewHolder חדש
     */
    @NonNull
    @Override
    public RecyclerAdapterLibraryOperation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_operation_for_lv, parent, false);
        return new ViewHolder(view);
    }

    /**
     * פעולה שמקשרת בין הנתונים של פעולה אחת לרכיבי התצוגה.
     * גם מאזינה ללחיצה על הפריט ומעבירה למסך צפייה בפרטי פעולה.
     * @param holder ה-ViewHolder שאליו מקשרים את הנתונים
     * @param position מיקום הפריט ברשימה המסוננת
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLibraryOperation.ViewHolder holder, int position)
    {
        Operation operation = filteredList.get(position);
        holder.topic.setText(operation.getNameOfOperation());
        holder.time.setText(operation.getLengthOfOperation()+" דקות ");
        holder.age.setText(operation.getAge());
        holder.goals.setText(operation.getGoals());
        holder.userName.setText(operation.getUserName());
        holder.organization.setText(operation.getOrganization());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, View_operation.class);
                intent.putExtra("operationKey", operation.getKey());
                context.startActivity(intent);
            }
        });
    }

    /**
     * מחזירה את מספר הפריטים ברשימה המסוננת.
     * @return מספר הפעולות להצגה
     */
    @Override
    public int getItemCount() {
        if(filteredList == null)
            return 0;
        else
            return filteredList.size();
    }

    /**
     * מסנן את רשימת הפעולות על פי מחרוזת חיפוש.
     * מבצע השוואה מול שדות שונים של הפעולה.
     * @param query מחרוזת החיפוש של המשתמש
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filterSearch(String query) {
        filteredList.clear();
        if (query.isEmpty())
            filteredList.addAll(originalList);

        else {
            query = query.toLowerCase(); // It converts the search query (user input) to lowercase letters.

            for (Operation operation : originalList) {
                if (operation.getNameOfOperation().toLowerCase().contains(query) ||
                        String.valueOf(operation.getLengthOfOperation()).contains(query) ||
                        operation.getGoals().toLowerCase().contains(query) ||
                        operation.getOrganization().toLowerCase().contains(query) ||
                        operation.getUserName().toLowerCase().contains(query) ||
                        String.valueOf(operation.getAge()).contains(query)) {
                    filteredList.add(operation);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * מחלקת ViewHolder פנימית - מחזיקה את רכיבי התצוגה של כל פריט פעולה.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic; //שם הפעולה
        TextView time; //משך הפעולה
        TextView age; // גיל החניכים
        TextView goals; // מטרות הפעולה
        TextView userName; //שם המשתמש שפרסם את הפעולה
        TextView organization; // תנועת הנוער בה נמצא מפרסם הפעולה
        ConstraintLayout parentLayout; // הפריסה הכוללת של כל פריט ברשימה
        /**
         * פעולה בונה של ה-ViewHolder, קושרת את הרכיבים מה-XML.
         * @param itemView הפריט הבודד ברשימה
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            time = itemView.findViewById(R.id.time);
            age = itemView.findViewById(R.id.age);
            goals = itemView.findViewById(R.id.goals);
            userName = itemView.findViewById(R.id.writerUserName);
            organization = itemView.findViewById(R.id.organization);
            parentLayout = itemView.findViewById(R.id.one_operation_for_lv);
        }
        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }
        public TextView getOrganization() {
            return organization;
        }
        public TextView getTopic() {
            return topic;
        }
        public TextView getAge() {
            return age;
        }
        public TextView getGoals() {
            return goals;
        }
        public TextView getUserName() {
            return userName;
        }
        public TextView getTime() {
            return time;
        }
    }
}
