package com.example.guided.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerAdapterLibraryTrip;
import com.example.guided.Classes.Trip;

import java.util.ArrayList;

/**
 * Fragment זה מציג את רשימת הטיולים השמורים מה-Firebase.
 * כולל תמיכה בחיפוש לפי שם הטיול או תוכן.
 */
public class LibraryTripsFragment extends Fragment {
    private View v; // תצוגה (View) של ה-Fragment. משמשת לאחסון ה-View הראשי של ה-Fragment.
    /**
     * RecyclerView להצגת רשימת הטיולים (Trips).
     * משתמשים בו להציג את הנתונים ב-RecyclerView, עם אפשרות לגלול בין פריטים.
     */
    private RecyclerView recyclerView;
    /**
     * Adapter המתווך בין הנתונים המתקבלים ממסד הנתונים לבין רכיבי ה-RecyclerView.
     * אחראי להצגת הטיולים (Trips) ברשימה ולהגיב לשינויים בנתונים.
     */
    private RecyclerAdapterLibraryTrip recyclerAdapter;
    private ArrayList<Trip> tripsArrayList; // רשימה של טיולים (Trips) שהתקבלו ממסד הנתונים Firebase
    /**
     * רכיב חיפוש (SearchView) המאפשר למשתמש לחפש טיולים ברשימה.
     * מחפש טקסט בתוך רשימת הטיולים ומסנן את התוצאות.
     */
    private android.widget.SearchView search;

    /**
     * בנאי ברירת מחדל נדרש עבור Fragment.
     * נדרש על ידי מערכת אנדרואיד לצורך יצירת מופע של המחלקה בעת שחזור מצב (כמו בסיבוב מסך).
     */
    public LibraryTripsFragment() {
    }

    /**
     * פעולה זו יוצרת את ממשק המשתמש של ה-Fragment, מאתחלת את רשימת הטיולים,
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
                R.layout.fragment_library_trips,
                container,
                false);

        if ( v != null)
        {
            search = v.findViewById(R.id.search);

            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
            fireBaseTripHelper.fetchTrips(
                    new FireBaseTripHelper.DataStatus()
                    {
                        /**
                         * פעולה זו מופעלת לאחר טעינת הנתונים מה-Database.
                         * מאתחלת את הרשימה והמתאם ומפעילה חיפוש.
                         * @param trips רשימת טיולים שהתקבלה מה-Database
                         */
                        @Override
                        public void onDataLoaded(ArrayList<Trip> trips) {
                            tripsArrayList = new ArrayList<>();
                            for (int i = trips.size()-1; i>=0; i--){
                                tripsArrayList.add(trips.get(i));
                            }
                            recyclerAdapter = new RecyclerAdapterLibraryTrip(
                                    getContext(),
                                    tripsArrayList);
                            recyclerView.setAdapter(recyclerAdapter);

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