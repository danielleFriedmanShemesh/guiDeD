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


public class ProfileFragment extends Fragment implements View.OnClickListener {
    View v;
    ImageView addOperationBTN;
    ImageView addTripBTN;
    ConstraintLayout see_more_operation;
    ConstraintLayout see_more_trip;
    TextView sumTr;
    TextView sumOp;
    ImageView profile;
    TextView organization;
    TextView nickName;
    TextView userName;
    ImageView editInfo;

    User user;

//TODO: לעשות שאחרי הוספה של פעולות יעשה ריפרש ויראה ישר את הפעולה החדשה (כשיש פחות מ4)

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_profile, container, false);

        if ( v != null)
        {

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


            ProfileFragment.LayoutViewOp layout1Views = new LayoutViewOp(
                    v.findViewById(R.id.topic1),
                    v.findViewById(R.id.ageOp1),
                    v.findViewById(R.id.goals1),
                    v.findViewById(R.id.time1),
                    v.findViewById(R.id.clOp1)
            );
            ProfileFragment.LayoutViewOp layout2Views = new LayoutViewOp(
                    v.findViewById(R.id.topic2),
                    v.findViewById(R.id.ageOp2),
                    v.findViewById(R.id.goals2),
                    v.findViewById(R.id.time2),
                    v.findViewById(R.id.clOp2)
            );
            ProfileFragment.LayoutViewOp layout3Views = new LayoutViewOp(
                    v.findViewById(R.id.topic3),
                    v.findViewById(R.id.ageOp3),
                    v.findViewById(R.id.goals3),
                    v.findViewById(R.id.time3),
                    v.findViewById(R.id.clOp3)
            );
            ProfileFragment.LayoutViewOp layout4Views = new LayoutViewOp(
                    v.findViewById(R.id.topic4),
                    v.findViewById(R.id.ageOp4),
                    v.findViewById(R.id.goals4),
                    v.findViewById(R.id.time4),
                    v.findViewById(R.id.clOp4)
            );


            ProfileFragment.LayoutViewsTr layout1ViewsTrip = new LayoutViewsTr(
                    v.findViewById(R.id.title1),
                    v.findViewById(R.id.age1),
                    v.findViewById(R.id.area1),
                    v.findViewById(R.id.length1),
                    v.findViewById(R.id.place1),
                    v.findViewById(R.id.clTr1)
            );
            ProfileFragment.LayoutViewsTr layout2ViewsTrip = new LayoutViewsTr(
                    v.findViewById(R.id.title2),
                    v.findViewById(R.id.age2),
                    v.findViewById(R.id.area2),
                    v.findViewById(R.id.length2),
                    v.findViewById(R.id.place2),
                    v.findViewById(R.id.clTr2)
            );
            ProfileFragment.LayoutViewsTr layout3ViewsTrip = new LayoutViewsTr(
                    v.findViewById(R.id.title3),
                    v.findViewById(R.id.age3),
                    v.findViewById(R.id.area3),
                    v.findViewById(R.id.length3),
                    v.findViewById(R.id.place3),
                    v.findViewById(R.id.clTr3)
            );
            ProfileFragment.LayoutViewsTr layout4ViewsTrip = new LayoutViewsTr(
                    v.findViewById(R.id.title4),
                    v.findViewById(R.id.age4),
                    v.findViewById(R.id.area4),
                    v.findViewById(R.id.length4),
                    v.findViewById(R.id.place4),
                    v.findViewById(R.id.clTr4)
            );


            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
                @Override
                public void onUserDataLoaded(User u) {
                    user = u;
                    organization.setText(user.getOrganization());
                    userName.setText(user.getUserName());
                    nickName.setText(user.getNickName());
                    profile.setImageBitmap(BitmapHelper.stringToBitmap(user.getProfileImage()));


                    FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
                    fireBaseTripHelper.fetchTrips(
                            new FireBaseTripHelper.DataStatus() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataLoaded(ArrayList<Trip> trips) {
                                    ArrayList<Trip> tripArrayList =  new ArrayList<>();
                                    for (int i = 0; i<trips.size(); i++){
                                        Trip trip = trips.get(i);
                                        if (trip.getUserName().equals(user.getUserName())) {
                                            tripArrayList.add(trip);
                                        }
                                    }
                                    int size = tripArrayList.size();

                                    if (size == 0){
                                        layout4ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout3ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout2ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout1ViewsTrip.getParentLayout().setVisibility(View.GONE);

                                    }
                                    else if (size == 1){
                                        layout4ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout3ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout2ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        Trip trip1 = tripArrayList.get(0);
                                        displayTrInLayout(trip1, layout1ViewsTrip);
                                    }
                                    else if (size == 2){
                                        layout4ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        layout3ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        Trip trip1 = tripArrayList.get(0);
                                        Trip trip2 = tripArrayList.get(1);
                                        displayTrInLayout(trip1, layout1ViewsTrip);
                                        displayTrInLayout(trip2, layout2ViewsTrip);
                                    }
                                    else if (size == 3){
                                        layout4ViewsTrip.getParentLayout().setVisibility(View.GONE);
                                        Trip trip1 = tripArrayList.get(2);
                                        Trip trip2 = tripArrayList.get(1);
                                        Trip trip3 = tripArrayList.get(0);
                                        displayTrInLayout(trip1, layout1ViewsTrip);
                                        displayTrInLayout(trip2, layout2ViewsTrip);
                                        displayTrInLayout(trip3, layout3ViewsTrip);
                                    }

                                    else {
                                        Trip trip1 = tripArrayList.get(size - 1);
                                        Trip trip2 = tripArrayList.get(size - 2);
                                        Trip trip3 = tripArrayList.get(size - 3);
                                        Trip trip4 = tripArrayList.get(size - 4);

                                        displayTrInLayout(trip1, layout1ViewsTrip);
                                        displayTrInLayout(trip2, layout2ViewsTrip);
                                        displayTrInLayout(trip3, layout3ViewsTrip);
                                        displayTrInLayout(trip4, layout4ViewsTrip);

                                    }
                                    sumTr.setText( tripArrayList.size() + " טיולים ");
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
                                    for (int i = 0; i<operations.size(); i++){
                                        Operation operation = operations.get(i);
                                        if (operation.getUserName().equals(user.getUserName())) {
                                            operationArrayList.add(operation);
                                        }
                                    }
                                    int size = operationArrayList.size();

                                    if (size == 0){
                                        layout4Views.getParentLayout().setVisibility(View.GONE);
                                        layout3Views.getParentLayout().setVisibility(View.GONE);
                                        layout2Views.getParentLayout().setVisibility(View.GONE);
                                        layout1Views.getParentLayout().setVisibility(View.GONE);

                                    }
                                    else if (size == 1){
                                        layout4Views.getParentLayout().setVisibility(View.GONE);
                                        layout3Views.getParentLayout().setVisibility(View.GONE);
                                        layout2Views.getParentLayout().setVisibility(View.GONE);
                                        Operation operation1 = operationArrayList.get(0);
                                        displayOpInLayout(operation1, layout1Views);
                                    }
                                    else if (size == 2){
                                        layout4Views.getParentLayout().setVisibility(View.GONE);
                                        layout3Views.getParentLayout().setVisibility(View.GONE);
                                        Operation operation1 = operationArrayList.get(1);
                                        Operation operation2 = operationArrayList.get(0);
                                        displayOpInLayout(operation1, layout1Views);
                                        displayOpInLayout(operation2, layout2Views);
                                    }
                                    else if (size == 3){
                                        layout4Views.getParentLayout().setVisibility(View.GONE);
                                        Operation operation1 = operationArrayList.get(2);
                                        Operation operation2 = operationArrayList.get(1);
                                        Operation operation3 = operationArrayList.get(0);
                                        displayOpInLayout(operation1, layout1Views);
                                        displayOpInLayout(operation2, layout2Views);
                                        displayOpInLayout(operation3, layout3Views);
                                    }

                                    if (size >= 4){

                                        Operation operation1 = operationArrayList.get(size - 1);
                                        Operation operation2 = operationArrayList.get(size - 2);
                                        Operation operation3 = operationArrayList.get(size - 3);
                                        Operation operation4 = operationArrayList.get(size - 4);

                                        displayOpInLayout(operation1, layout1Views);
                                        displayOpInLayout(operation2, layout2Views);
                                        displayOpInLayout(operation3, layout3Views);
                                        displayOpInLayout(operation4, layout4Views);

                                    }
                                    sumOp.setText(operationArrayList.size() + " פעולות ");
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

    @SuppressLint("SetTextI18n")
    private void displayOpInLayout(Operation operation, ProfileFragment.LayoutViewOp views) {
        views.topic.setText(operation.getNameOfOperation());
        views.time.setText(operation.getLengthOfOperation()+" דקות ");
        views.age.setText(operation.getAge());
        views.goals.setText(operation.getGoals());
        views.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Add_operation.class);
                intent.putExtra("operationKey", operation.getKey());
                startActivity(intent);

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayTrInLayout(Trip trip, ProfileFragment.LayoutViewsTr views) {
        views.title.setText(trip.getNameOfTrip());
        views.length.setText(trip.getLengthInKm()+" ק''מ ");
        views.age.setText(trip.getAge());
        views.area.setText(trip.getArea());
        views.place.setText(trip.getPlace());
        views.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Add_trip.class);
                intent.putExtra("tripKey", trip.getKey());
                startActivity(intent);

            }
        });
    }


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
            Intent intent = new Intent(v.getContext(), Edit_profile.class);
            startActivity(intent);
        }

        if (view == addOperationBTN){
            Intent intent;
            intent = new Intent(v.getContext(), Add_operation.class);
            startActivity(intent);
        }

        if (view == addTripBTN){
            Intent intent;
            intent = new Intent(v.getContext(), Add_trip.class);
            startActivity(intent);
        }

    }

    private static class LayoutViewOp{
        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        ConstraintLayout parentLayout;

        public LayoutViewOp(TextView topic, TextView age, TextView goals, TextView time, ConstraintLayout parentLayout) {
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
    private static class LayoutViewsTr {
        TextView title;
        TextView length;
        TextView age;
        TextView area;
        TextView place;
        ConstraintLayout parentLayout;

        public LayoutViewsTr(TextView topic, TextView age, TextView area,TextView lengh,TextView place, ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.area = area;
            this.length = lengh;
            this.place = place;
            this.title = topic;
        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }
    }
}