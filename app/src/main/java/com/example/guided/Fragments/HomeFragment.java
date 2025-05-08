package com.example.guided.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.Classes.Trip;
import com.example.guided.Activities.View_operation;
import com.example.guided.Activities.View_trip;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private View v; // תצוגה (View) של ה-Fragment. משמשת לאחסון ה-View הראשי של ה-Fragment.
    private ConstraintLayout see_more_operation; // פריסת ראה עוד של פעולות שלאחר לחיצה עליה נפתח מאגר כל הפעולות הציבוריות
    private ConstraintLayout see_more_trip; // פריסת ראה עוד של טיולים שלאחר לחיצה עליה נפתח מאגר כל הטיולים הציבוריים
    private ArrayList<LayoutViewsOperation> operationLayouts; // מערך תצוגות עבור פעולות
    private ArrayList<LayoutViewsTrip> tripLayouts; // מערך תצוגות עבור טיולים

    /**
     * בנאי ברירת מחדל נדרש עבור Fragment.
     * נדרש על ידי מערכת אנדרואיד לצורך יצירת מופע של המחלקה בעת שחזור מצב (כמו בסיבוב מסך).
     */
    public HomeFragment() {
    }

    /**
     * פעולה זו יוצרת את ה-View של הפרגמנט,
     * קושרת את הרכיבים ב-XML, מאזינים ללחיצות,
     * מציגה טיולים ופעולות ציבוריים שנשלפו מהdatabase.
     *
     * @param inflater           אינפלייטר ליצירת ה-View
     * @param container          מיכל שמכיל את ה-Fragment
     * @param savedInstanceState מידע שמור אם קיים
     * @return View של הפרגמנט
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        operationLayouts = createOperationLayouts(v);
        tripLayouts = createTripLayouts(v);

        see_more_operation = v.findViewById(R.id.seeMoreOp);
        see_more_operation.setOnClickListener(this);
        see_more_trip = v.findViewById(R.id.seeMoreTr);
        see_more_trip.setOnClickListener(this);

        FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
        fireBaseTripHelper.fetchTrips(
                new FireBaseTripHelper.DataStatus() {
                    @Override
                    public void onDataLoaded(ArrayList<Trip> trips) {
                        ArrayList<Trip> tripArrayList =  new ArrayList<>();

                        for (int i = trips.size()-1; i >= 0; i--){
                            Trip trip = trips.get(i);
                            if (trip.getPublicORprivate().equals("isPublic")) {
                                tripArrayList.add(trip);
                            }
                        }
                        if(!tripArrayList.isEmpty()) {
                            ArrayList<Trip> trs = new ArrayList<>();
                            for (int i = 0; i < Math.min(4, tripArrayList.size()); i++) {
                                trs.add(tripArrayList.get(i));
                            }
                            for (int i = 0; i < trs.size(); i++) {
                                displayTripInLayout(trs.get(i), tripLayouts.get(i));
                            }
                            if(trs.size() < 4){
                                for (int j = (trs.size()) ; j < 4; j++){
                                    tripLayouts.get(j).getParentLayout().setVisibility(View.GONE);
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < 4; i++){
                                tripLayouts.get(i).getParentLayout().setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );

        FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
        fireBaseOperationHelper.fetchOperations(
                new FireBaseOperationHelper.DataStatus()
                {
                    @Override
                    public void onDataLoaded(ArrayList<Operation> operations) {
                        ArrayList<Operation> operationArrayList =  new ArrayList<>();

                        for (int i = operations.size()-1 ; i>= 0 ; i--){
                            Operation operation = operations.get(i);
                            if (operation.getPrivateORpublic().equals("isPublic")) {
                                operationArrayList.add(operation);
                            }
                        }

                        if(!operationArrayList.isEmpty()) {
                            ArrayList<Operation> ops = new ArrayList<>();
                            for (int i = 0; i < Math.min(4, operationArrayList.size()); i++) {
                                ops.add(operationArrayList.get(i));
                            }
                            for (int i = 0; i < ops.size(); i++) {
                                displayOperationInLayout(ops.get(i), operationLayouts.get(i));
                            }
                            if(ops.size() < 4){
                                for (int j = (ops.size()) ; j < 4; j++){
                                    operationLayouts.get(j).getParentLayout().setVisibility(View.GONE);
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < 4; i++){
                                operationLayouts.get(i).getParentLayout().setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );
        return v;
    }

    /**
     * יוצרת רשימה של LayoutViewOp – אובייקטים שמייצגים את תצוגות הפעולות במסך הבית.
     * כל LayoutViewOp מכיל הפניות ל־TextViewים ול־ConstraintLayout שמייצגים פעולה אחת.
     * מחפש את ה־View-ים לפי מזהי ID דינמיים (topic1, ageOp1, goals1 וכו') עבור 4 פעולות.
     *
     * @param v תצוגת הפרגמנט שמכיל את כל רכיבי התצוגה
     * @return רשימת LayoutViewOp המכילה 4 תצוגות פעולה
     */
    private ArrayList<LayoutViewsOperation> createOperationLayouts(View v){
        ArrayList<LayoutViewsOperation> layoutViewOps = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            layoutViewOps.add(new LayoutViewsOperation(
                    v.findViewById(getResId("topic" + i)),
                    v.findViewById(getResId("ageOp" + i)),
                    v.findViewById(getResId("goals" + i)),
                    v.findViewById(getResId("time" + i)),
                    v.findViewById(getResId("organizationOP" + i)),
                    v.findViewById(getResId("writerUserName" + i)),
                    v.findViewById(getResId("clOp" + i))
            ));
        }
        return layoutViewOps;
    }
    /**
     * יוצרת רשימה של LayoutViewsTr – אובייקטים שמייצגים את תצוגות הטיולים במסך הבית.
     * כל LayoutViewsTr מכיל הפניות ל־TextViewים ול־ConstraintLayout שמייצגים טיול אחד.
     * מחפש את ה־View-ים לפי מזהי ID דינמיים (title1, age1, area1 וכו') עבור 4 טיולים.
     *
     * @param v תצוגת הפרגמנט שמכיל את כל רכיבי התצוגה
     * @return רשימת LayoutViewsTr המכילה 4 תצוגות טיול
     */
    private ArrayList<LayoutViewsTrip> createTripLayouts(View v){
        ArrayList<LayoutViewsTrip> layoutViewTrs = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            layoutViewTrs.add(new LayoutViewsTrip(
                    v.findViewById(getResId("title" + i)),
                    v.findViewById(getResId("age" + i)),
                    v.findViewById(getResId("area" + i)),
                    v.findViewById(getResId("length" + i)),
                    v.findViewById(getResId("organization" + i)),
                    v.findViewById(getResId("id" + i)),
                    v.findViewById(getResId("clTr" + i))
            ));
        }
        return layoutViewTrs;
    }

    /**
     * מחזיר את המזהה (ID) של רכיב תצוגה לפי שמו (name) מתוך קובץ ה־XML.
     * מאפשר גישה דינמית למשאבים – לדוגמה topic1, topic2 וכו'.
     *
     * @param name שם המשאב (למשל "topic1", "age3")
     * @return מזהה המשאב (int) כפי שמוגדר ב־R.id
     */
    private int getResId(String name) {
        return v.getResources().getIdentifier(name, "id", requireContext().getPackageName());
    }

    /**
     * מציגה פעולה (Operation) ב־Layout ספציפי של פעולה במסך הבית.
     * מעדכנת את שדות ה־TextView בהתאם לנתוני הפעולה, וקובעת האזנה ללחיצה.
     * בעת לחיצה על התצוגה – תיפתח פעילות ההצגה View_operation עם מפתח הפעולה.
     *
     * @param randomOperation הפעולה שברצוננו להציג
     * @param views     LayoutViewOp המכיל את הרכיבים הגרפיים להצגת הפעולה
     */
    @SuppressLint("SetTextI18n")
    private void displayOperationInLayout(Operation randomOperation, LayoutViewsOperation views) {
        views.topic.setText(randomOperation.getNameOfOperation());
        views.time.setText(randomOperation.getLengthOfOperation()+" דקות ");
        views.age.setText(randomOperation.getAge());
        views.goals.setText(randomOperation.getGoals());
        views.userName.setText(randomOperation.getUserName());
        views.organization.setText(randomOperation.getOrganization());
        views.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), View_operation.class);
                intent.putExtra("operationKey", randomOperation.getKey());
                startActivity(intent);

            }
        });
    }

    /**
     * מציגה טיול (Trip) ב־Layout ספציפי של טיול במסך הבית.
     * מעדכנת את שדות ה־TextView בהתאם לנתוני הטיול, וקובעת האזנה ללחיצה.
     * בעת לחיצה על התצוגה – תיפתח פעילות ההצגה View_trip עם מפתח הטיול.
     *
     * @param randomTrip  הטיול שברצוננו להציג
     * @param views LayoutViewsTr המכיל את הרכיבים הגרפיים להצגת הטיול
     */
    @SuppressLint("SetTextI18n")
    private void displayTripInLayout(Trip randomTrip, LayoutViewsTrip views) {
        views.title.setText(randomTrip.getNameOfTrip());
        views.lengh.setText(randomTrip.getLengthInKm()+" ק''מ ");
        views.age.setText(randomTrip.getAge());
        views.area.setText(randomTrip.getArea());
        views.userName.setText(randomTrip.getUserName());
        views.organization.setText(randomTrip.getOrganization());
        views.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), View_trip.class);
                intent.putExtra("tripKey", randomTrip.getKey());
                startActivity(intent);

            }
        });
    }

    /**
     * מאזין לאירועים מכלל כפתורי הלחיצה במסך הבית.
     * כל לחיצה על כפתור מפעילה את הפעולה המתאימה:
     * - מעבר למאגר הפעולות או הטיולים (Fragments)*
     * @param view הרכיב שנלחץ
     */
    @Override
    public void onClick(View view) {
        if (view == see_more_operation){
            Fragment newFragment = new LibraryOperationsFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, newFragment);
            transaction.addToBackStack(null); //adds to back stack so user can go back
            transaction.commit();
        }
        else if (view == see_more_trip) {
            Fragment newFragment = new LibraryTripsFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, newFragment);
            transaction.addToBackStack(null); //adds to back stack so user can go back
            transaction.commit();
        }

    }

    /**
     * מחלקה פנימית המייצגת תצוגת פעולה אחת במסך הבית.
     * מכילה הפניות לרכיבי TextView ול־ConstraintLayout מתוך קובץ ה־XML,
     * המאפשרים להציג פעולה ציבורית.
     */
    private static class LayoutViewsOperation {
        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        TextView userName;
        TextView organization;
        ConstraintLayout parentLayout;

        public LayoutViewsOperation(TextView topic, TextView age, TextView goals,TextView time,TextView organization, TextView userName,ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.goals = goals;
            this.time = time;
            this.organization = organization;
            this.topic = topic;
            this.userName = userName;
        }

        public TextView getAge() {
            return age;
        }

        public TextView getGoals() {
            return goals;
        }

        public TextView getOrganization() {
            return organization;
        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getTopic() {
            return topic;
        }

        public TextView getUserName() {
            return userName;
        }
    }

    /**
     * מחלקה פנימית המייצגת תצוגת טיול אחד במסך הבית.
     * מכילה הפניות לרכיבי TextView ול־ConstraintLayout מתוך קובץ ה־XML,
     * המאפשרים להציג טיול ציבורי.
     */
    private static class LayoutViewsTrip {
        TextView title;
        TextView lengh;
        TextView age;
        TextView area;
        TextView userName;
        TextView organization;
        ConstraintLayout parentLayout;

        public LayoutViewsTrip(TextView topic, TextView age, TextView area,TextView lengh,TextView organization, TextView userName,ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.area = area;
            this.lengh = lengh;
            this.organization = organization;
            this.title = topic;
            this.userName = userName;
        }

        public TextView getAge() {
            return age;
        }

        public TextView getArea() {
            return area;
        }

        public TextView getLengh() {
            return lengh;
        }

        public TextView getOrganization() {
            return organization;
        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getUserName() {
            return userName;
        }
    }
}