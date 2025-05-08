package com.example.guided.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerMyOperationsAdapter;
import com.example.guided.Classes.User;

import java.util.ArrayList;

/**
 * מחלקה המציגה את רשימת הפעולות של המשתמש הנוכחי מתוך Firebase.
 */
public class My_operations extends Fragment implements View.OnClickListener {
    private View v; // תצוגה (View) של ה-Fragment. משמשת לאחסון ה-View הראשי של ה-Fragment.

    private ImageView backBTN; // כפתור החרה למסך הקודם
    /**
     * RecyclerView להצגת רשימת הפעולות (Operations).
     * משתמשים בו להציג את הנתונים ב-RecyclerView, עם אפשרות לגלול בין פריטים.
     */
    private RecyclerView recyclerView;
    /**
     * Adapter המתווך בין הנתונים המתקבלים ממסד הנתונים לבין רכיבי ה-RecyclerView.
     * אחראי להצגת הפעולות (Operations) ברשימה ולהגיב לשינויים בנתונים.
     */
    private RecyclerMyOperationsAdapter recyclerAdapter;
    private ArrayList<Operation> operationArrayList; // רשימה של פעולות (Operations) שהתקבלו ממסד הנתונים Firebase

    /**
     * בנאי ברירת מחדל נדרש עבור Fragment.
     * נדרש על ידי מערכת אנדרואיד לצורך יצירת מופע של המחלקה בעת שחזור מצב (כמו בסיבוב מסך).
     */
    public My_operations() {
    }

    /**
     * פעולה זו יוצרת את ממשק המשתמש של ה-Fragment, ומאתחלת את רשימת הפעולות,
     * @param inflater משתנה ליצירת תצוגה מתוך קובץ XML
     * @param container הקונטיינר של ה-Fragment
     * @param savedInstanceState מידע שמור (אם קיים)
     * @return התצוגה הראשית של ה-Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_operations, container, false);
        if (v != null){

            backBTN = v.findViewById(R.id.back);
            backBTN.setOnClickListener(this);

            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

            // הבאת נתוני משתמש והצגת פעולות
            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
                @Override
                public void onUserDataLoaded(User u) {
                    FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
                    fireBaseOperationHelper.fetchOperations(
                            new FireBaseOperationHelper.DataStatus()
                            {

                                @Override
                                public void onDataLoaded(ArrayList<Operation> operations) {
                                    operationArrayList = operations;

                                    recyclerAdapter = new RecyclerMyOperationsAdapter(getContext(),operationArrayList, u);
                                    recyclerView.setAdapter(recyclerAdapter);
                                }
                            }
                    );
                }
                @Override
                public void onError(String errorMessage) {
                }
            });
        }
        return v;
    }

    /**
     * מאזין ללחיצה על כפתור החזרה – חוזר לפרגמנט הקודם.
     * @param v הרכיב שנלחץ.
     */
    @Override
    public void onClick(View v) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}