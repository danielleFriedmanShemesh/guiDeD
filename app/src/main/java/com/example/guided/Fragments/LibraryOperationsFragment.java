package com.example.guided.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerAdapterLibraryOperation;

import java.util.ArrayList;

/**
 * Fragment זה מציג את רשימת הפעולות השמורות מה-Firebase.
 * כולל תמיכה בחיפוש לפי שם הפעולה או תוכן.
 */
public class LibraryOperationsFragment extends Fragment {
    private View v; // תצוגה (View) של ה-Fragment. משמשת לאחסון ה-View הראשי של ה-Fragment.
    /**
     * RecyclerView להצגת רשימת הפעולות (Operations).
     * משתמשים בו להציג את הנתונים ב-RecyclerView, עם אפשרות לגלול בין פריטים.
     */
    private RecyclerView recyclerView;
    /**
     * Adapter המתווך בין הנתונים המתקבלים ממסד הנתונים לבין רכיבי ה-RecyclerView.
     * אחראי להצגת הפעולות (Operations) ברשימה ולהגיב לשינויים בנתונים.
     */
    private RecyclerAdapterLibraryOperation recyclerAdapter;
    private ArrayList<Operation> operationArrayList; // רשימה של פעולות (Operations) שהתקבלו ממסד הנתונים Firebase
    /**
     * רכיב חיפוש (SearchView) המאפשר למשתמש לחפש פעולות ברשימה.
     * מחפש טקסט בתוך רשימת הפעולות ומסנן את התוצאות.
     */
    private android.widget.SearchView search;

    /**
     * בנאי ברירת מחדל נדרש עבור Fragment.
     * נדרש על ידי מערכת אנדרואיד לצורך יצירת מופע של המחלקה בעת שחזור מצב (כמו בסיבוב מסך).
     */
    public LibraryOperationsFragment() {
    }

    /**
     * פעולה זו יוצרת את ממשק המשתמש של ה-Fragment, מאתחלת את רשימת הפעולות,
     * ומגדירה את יכולת החיפוש.
     * @param inflater משתנה ליצירת תצוגה מתוך קובץ XML
     * @param container הקונטיינר של ה-Fragment
     * @param savedInstanceState מידע שמור (אם קיים)
     * @return התצוגה הראשית של ה-Fragment
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(
                R.layout.fragment_library_operations,
                container,
                false);
        if ( v != null) {
            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
            fireBaseOperationHelper.fetchOperations(
                    new FireBaseOperationHelper.DataStatus()
                    {
                        /**
                         * פעולה זו מופעלת לאחר טעינת הנתונים מה-Database.
                         * מאתחלת את הרשימה והמתאם ומפעילה חיפוש.
                         * @param operations רשימת פעולות שהתקבלה מה-Database
                         */
                    @Override
                    public void onDataLoaded(ArrayList<Operation> operations) {
                        operationArrayList = new ArrayList<>();
                        for (int i = operations.size()-1; i>=0; i--){
                            operationArrayList.add(operations.get(i));
                        }
                        recyclerAdapter = new RecyclerAdapterLibraryOperation(
                                getContext(),
                                operationArrayList);
                        recyclerView.setAdapter(recyclerAdapter);

                        search = v.findViewById(R.id.search);
                        search.setOnQueryTextListener(
                                new android.widget.SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                recyclerAdapter.filterSearch(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                recyclerAdapter.filterSearch(newText);
                                return false;
                            }
                        });
                    }
                }
            );
        }
        return v;
    }
}