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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guided.Activities.Add_operation;
import com.example.guided.Activities.Add_trip;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Activities.Edit_profile;
import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.Classes.Trip;
import com.example.guided.Classes.User;

import java.util.ArrayList;

/**
 * פרגמנט המציג את מסך הפרופיל של המשתמש.
 * מאפשר צפייה בפרטי המשתמש, בטיולים ופעולות שיצר, עריכת פרופיל, והוספת תכנים.
 * הנתונים נטענים מ-Firebase Realtime Database
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View v; // תצוגה (View) של ה-Fragment. משמשת לאחסון ה-View הראשי של ה-Fragment.
    private ImageView addOperationBTN; // כפתור הוספת פעולה חדשה
    private ImageView addTripBTN; // כפתור הוספת טיול חדש
    private ConstraintLayout see_more_operation; // פריסת ראה עוד של פעולות שלאחר לחיצה עליה נפתח מסך הצגת כל הפעולות של המשתמש
    private ConstraintLayout see_more_trip; // פריסת ראה עוד של טיולים שלאחר לחיצה עליה נפתח מסך הצגת כל הטיולים של המשתמש
    private TextView sumTr; // מציג את סך הטיולים שיצר המשתמש
    private TextView sumOp; // מציג את סך הפעולות שיצר המשתמש
    private ImageView profile; // מציג את תמונת הפרופיל של המשתמש
    private TextView organization; // מציג את תנועת הנוער של המשתמש
    private TextView nickName; // מציג את הכינוי של המשתמש
    private TextView userName; //מציג את שם המשתמש
    private ImageView editInfo; // כפתור לפתיחת מסך עריכת פרטים אישיים

    private ArrayList<LayoutViewOp> operationLayouts; // מערך תצוגות עבור פעולות
    private ArrayList<LayoutViewsTr> tripLayouts; // מערך תצוגות עבור טיולים

    private User user; // אובייקט המשתמש הנוכחי

//TODO: לעשות שאחרי הוספה של פעולות יעשה ריפרש ויראה ישר את הפעולה החדשה (כשיש פחות מ4)

    /**
     * בנאי ברירת מחדל נדרש עבור Fragment.
     * נדרש על ידי מערכת אנדרואיד לצורך יצירת מופע של המחלקה בעת שחזור מצב (כמו בסיבוב מסך).
     */
    public ProfileFragment() {
    }

    /**
     * פעולה זו יוצרת את ה-View של הפרגמנט,
     * קושרת את הרכיבים ב-XML, מאזינים ללחיצות, טוענת את נתוני המשתמש,
     * מציגה טיולים ופעולות אחרונים ומעדכנת את סיכומם.
     *
     * @param inflater           אינפלייטר ליצירת ה-View
     * @param container          מיכל שמכיל את ה-Fragment
     * @param savedInstanceState מידע שמור אם קיים
     * @return View של הפרגמנט
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
         v = inflater.inflate(
                 R.layout.fragment_profile,
                 container,
                 false);

        if ( v != null)
        {
            operationLayouts = createOperationLayouts(v);
            tripLayouts = createTripLayouts(v);

            sumOp = v.findViewById(R.id.sumOp);
            sumTr = v.findViewById(R.id.sumTr);

            organization = v.findViewById(R.id.organization);
            profile = v.findViewById(R.id.profile);
            nickName = v.findViewById(R.id.nickName);
            userName = v.findViewById(R.id.userName);

            editInfo = v.findViewById(R.id.editProfile);
            editInfo.setOnClickListener(this);

            see_more_operation = v.findViewById(R.id.seeMoreOp);
            see_more_operation.setOnClickListener(this);
            see_more_trip = v.findViewById(R.id.seeMoreTr);
            see_more_trip.setOnClickListener(this);

            addOperationBTN = v.findViewById(R.id.add_operation);
            addOperationBTN.setOnClickListener(this);
            addTripBTN = v.findViewById(R.id.add_trip);
            addTripBTN.setOnClickListener(this);

            // שליפת נתוני המשתמש
            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUserData(
                    new FirebaseUserHelper.UserDataCallback() {
                @Override
                public void onUserDataLoaded(User u) {
                    user = u;
                    organization.setText(user.getOrganization());
                    userName.setText(user.getUserName());
                    nickName.setText(user.getNickName());
                    profile.setImageBitmap(
                            BitmapHelper.stringToBitmap(user.
                                    getProfileImage()));

                    FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
                    fireBaseTripHelper.fetchTrips(
                            new FireBaseTripHelper.DataStatus() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataLoaded(ArrayList<Trip> trips) {
                                    ArrayList<Trip> tripArrayList =  new ArrayList<>();
                                    for (int i = trips.size()-1; i >= 0; i--){
                                        Trip trip = trips.get(i);
                                        if (trip.getUserName().equals(user.getUserName()))
                                            tripArrayList.add(trip);
                                    }
                                    if(!tripArrayList.isEmpty()) {
                                        ArrayList<Trip> trs = new ArrayList<>();

                                        for (int i = 0; i < Math.min(4, tripArrayList.size()); i++)
                                            trs.add(tripArrayList.get(i));

                                        for (int i = 0; i < trs.size(); i++)
                                            displayTrInLayout(trs.get(i),
                                                    tripLayouts.get(i));

                                        if(trs.size() < 4){
                                            for (int j = (trs.size()) ; j < 4; j++)
                                                tripLayouts.
                                                        get(j).
                                                        getParentLayout().
                                                        setVisibility(
                                                                View.GONE);
                                        }
                                    }
                                    else {
                                        for (int i = 0; i < 4; i++)
                                            tripLayouts.
                                                    get(i).
                                                    getParentLayout().
                                                    setVisibility(
                                                            View.GONE);
                                    }
                                    sumTr.setText(
                                            tripArrayList.size() +
                                                    " טיולים ");
                                }
                            }
                    );

                    FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
                    fireBaseOperationHelper.fetchOperations(
                            new FireBaseOperationHelper.DataStatus()
                            {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataLoaded(ArrayList<Operation> operations) {
                                    ArrayList<Operation> operationArrayList =  new ArrayList<>();
                                    for (int i = operations.size()-1 ; i>= 0 ; i--){
                                        Operation operation = operations.get(i);
                                        if (operation.getUserName().equals(user.getUserName()))
                                            operationArrayList.add(operation);
                                    }

                                    if(!operationArrayList.isEmpty()) {
                                        ArrayList<Operation> ops = new ArrayList<>();

                                        for (int i = 0; i < Math.min(4, operationArrayList.size()); i++)
                                            ops.add(operationArrayList.get(i));

                                        for (int i = 0; i < ops.size(); i++)
                                            displayOpInLayout(ops.get(i), operationLayouts.get(i));

                                        if(ops.size() < 4){
                                            for (int j = (ops.size()) ; j < 4; j++)
                                                operationLayouts.
                                                        get(j).
                                                        getParentLayout().
                                                        setVisibility(
                                                                View.GONE);
                                        }
                                    }
                                    else {
                                        for (int i = 0; i < 4; i++)
                                            operationLayouts.
                                                    get(i).
                                                    getParentLayout().
                                                    setVisibility(
                                                            View.GONE);
                                    }
                                    sumOp.setText(
                                            operationArrayList.size() +
                                                    " פעולות ");
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
     * יוצרת רשימה של LayoutViewOp – אובייקטים שמייצגים את תצוגות הפעולות במסך הפרופיל.
     * כל LayoutViewOp מכיל הפניות ל־TextViewים ול־ConstraintLayout שמייצגים פעולה אחת.
     * מחפש את ה־View-ים לפי מזהי ID דינמיים (topic1, ageOp1, goals1 וכו') עבור 4 פעולות.
     *
     * @param v תצוגת הפרגמנט שמכיל את כל רכיבי התצוגה
     * @return רשימת LayoutViewOp המכילה 4 תצוגות פעולה
     */
    private ArrayList<LayoutViewOp> createOperationLayouts(View v){
        ArrayList<LayoutViewOp> layoutViewOps = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            layoutViewOps.add(new LayoutViewOp(
                    v.findViewById(getResId("topic" + i)),
                    v.findViewById(getResId("ageOp" + i)),
                    v.findViewById(getResId("goals" + i)),
                    v.findViewById(getResId("time" + i)),
                    v.findViewById(getResId("clOp" + i))
            ));
        }
        return layoutViewOps;
    }

    /**
     * יוצרת רשימה של LayoutViewsTr – אובייקטים שמייצגים את תצוגות הטיולים במסך הפרופיל.
     * כל LayoutViewsTr מכיל הפניות ל־TextViewים ול־ConstraintLayout שמייצגים טיול אחד.
     * מחפש את ה־View-ים לפי מזהי ID דינמיים (title1, age1, area1 וכו') עבור 4 טיולים.
     *
     * @param v תצוגת הפרגמנט שמכיל את כל רכיבי התצוגה
     * @return רשימת LayoutViewsTr המכילה 4 תצוגות טיול
     */
    private ArrayList<LayoutViewsTr> createTripLayouts(View v){
        ArrayList<LayoutViewsTr> layoutViewTrs = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            layoutViewTrs.add(new LayoutViewsTr(
                    v.findViewById(getResId("title" + i)),
                    v.findViewById(getResId("age" + i)),
                    v.findViewById(getResId("area" + i)),
                    v.findViewById(getResId("length" + i)),
                    v.findViewById(getResId("place" + i)),
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
        return v.
                getResources().
                getIdentifier(
                        name,
                        "id",
                        requireContext().
                                getPackageName());
    }


    /**
     * מציגה פעולה (Operation) ב־Layout ספציפי של פעולה במסך הפרופיל.
     * מעדכנת את שדות ה־TextView בהתאם לנתוני הפעולה, וקובעת האזנה ללחיצה.
     * בעת לחיצה על התצוגה – תיפתח פעילות העריכה Add_operation עם מפתח הפעולה.
     *
     * @param operation הפעולה שברצוננו להציג
     * @param views     LayoutViewOp המכיל את הרכיבים הגרפיים להצגת הפעולה
     */
    @SuppressLint("SetTextI18n")
    private void displayOpInLayout(
            Operation operation,
            ProfileFragment.LayoutViewOp views) {
        views.topic.setText(operation.getNameOfOperation());
        views.time.setText(operation.getLengthOfOperation()+" דקות ");
        views.age.setText(operation.getAge());
        views.goals.setText(operation.getGoals());
        views.parentLayout.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        v.getContext(),
                        Add_operation.class);
                intent.putExtra("operationKey", operation.getKey());
                startActivity(intent);
            }
        });
    }

    /**
     * מציגה טיול (Trip) ב־Layout ספציפי של טיול במסך הפרופיל.
     * מעדכנת את שדות ה־TextView בהתאם לנתוני הטיול, וקובעת האזנה ללחיצה.
     * בעת לחיצה על התצוגה – תיפתח פעילות העריכה Add_trip עם מפתח הטיול.
     *
     * @param trip  הטיול שברצוננו להציג
     * @param views LayoutViewsTr המכיל את הרכיבים הגרפיים להצגת הטיול
     */
    @SuppressLint("SetTextI18n")
    private void displayTrInLayout(
            Trip trip,
            ProfileFragment.LayoutViewsTr views) {
        views.title.setText(trip.getNameOfTrip());
        views.length.setText(trip.getLengthInKm()+" ק''מ ");
        views.age.setText(trip.getAge());
        views.area.setText(trip.getArea());
        views.place.setText(trip.getPlace());
        views.parentLayout.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        v.getContext(),
                        Add_trip.class);
                intent.putExtra("tripKey", trip.getKey());
                startActivity(intent);
            }
        });
    }

    /**
     * מאזין לאירועים מכלל כפתורי הלחיצה במסך הפרופיל.
     * כל לחיצה על כפתור מפעילה את הפעולה המתאימה:
     * - מעבר לרשימת הפעולות או הטיולים (Fragments)
     * - פתיחת מסך עריכת פרופיל
     * - הוספת פעולה חדשה או טיול חדש
     *
     * @param view הרכיב שנלחץ
     */
    @Override
    public void onClick(View view) {
        if(view == see_more_operation){
            Fragment newFragment = new My_operations();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, newFragment);
            transaction.addToBackStack(null); //adds to back stack so user can go back
            transaction.commit();
        }

        if (view == see_more_trip) {
            Fragment newFragment = new My_trips();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, newFragment);
            transaction.addToBackStack(null); //adds to back stack so user can go back
            transaction.commit();
        }

        if(view == editInfo){
            Intent intent = new Intent(
                    v.getContext(),
                    Edit_profile.class);
            startActivity(intent);
        }

        if (view == addOperationBTN){
            Intent intent;
            intent = new Intent(
                    v.getContext(),
                    Add_operation.class);
            startActivity(intent);
        }

        if (view == addTripBTN){
            Intent intent;
            intent = new Intent(
                    v.getContext(),
                    Add_trip.class);
            startActivity(intent);
        }
    }

    /**
     * מחלקה פנימית המייצגת תצוגת פעולה אחת במסך הפרופיל.
     * מכילה הפניות לרכיבי TextView ול־ConstraintLayout מתוך קובץ ה־XML,
     * המאפשרים להציג פעולה של המשתמש.
     */
    private static class LayoutViewOp{
        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        ConstraintLayout parentLayout;

        public LayoutViewOp(
                TextView topic,
                TextView age,
                TextView goals,
                TextView time,
                ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.goals = goals;
            this.time = time;
            this.topic = topic;
        }
        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }
    }

    /**
     * מחלקה פנימית המייצגת תצוגת טיול אחד במסך הפרופיל.
     * מכילה הפניות לרכיבי TextView ול־ConstraintLayout מתוך קובץ ה־XML,
     * המאפשרים להציג טיול של המשתמש.
     */
    private static class LayoutViewsTr {
        TextView title;
        TextView length;
        TextView age;
        TextView area;
        TextView place;
        ConstraintLayout parentLayout;

        public LayoutViewsTr(
                TextView topic,
                TextView age,
                TextView area,
                TextView length,
                TextView place,
                ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.area = area;
            this.length = length;
            this.place = place;
            this.title = topic;
        }
        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }
    }
}