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
import java.util.Random;


public class HomeFragment extends Fragment implements View.OnClickListener {
    View v;
    ConstraintLayout see_more_operation;
    ConstraintLayout see_more_trip;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

//        //לנסות להוסיף את הצפיה הראשונית בפעולה\טיול באופן דינאמי
//        for(int i=1; i<=5;i++){
//            ConstraintLayout constraintLayoutOp;
//            constraintLayoutOp = v.findViewById(R.id.one_operation_for_home_page);
//            constraintLayoutOp.setId(View.generateViewId());
//
//        }

        see_more_operation = v.findViewById(R.id.seeMoreOp);
        see_more_operation.setOnClickListener(this);
        see_more_trip = v.findViewById(R.id.seeMoreTr);
        see_more_trip.setOnClickListener(this);

        LayoutViewsOperation layout1Views = new LayoutViewsOperation(
                v.findViewById(R.id.topic1),
                v.findViewById(R.id.ageOp1),
                v.findViewById(R.id.goals1),
                v.findViewById(R.id.time1),
                v.findViewById(R.id.organizationOP1),
                v.findViewById(R.id.writerUserName1),
                v.findViewById(R.id.clOp1)
        );
        LayoutViewsOperation layout2Views = new LayoutViewsOperation(
                v.findViewById(R.id.topic2),
                v.findViewById(R.id.ageOp2),
                v.findViewById(R.id.goals2),
                v.findViewById(R.id.time2),
                v.findViewById(R.id.organizationOP2),
                v.findViewById(R.id.writerUserName2),
                v.findViewById(R.id.clOp2)
        );
        LayoutViewsOperation layout3Views = new LayoutViewsOperation(
                v.findViewById(R.id.topic3),
                v.findViewById(R.id.ageOp3),
                v.findViewById(R.id.goals3),
                v.findViewById(R.id.time3),
                v.findViewById(R.id.organizationOP3),
                v.findViewById(R.id.writerUserName3),
                v.findViewById(R.id.clOp3)
        );
        LayoutViewsOperation layout4Views = new LayoutViewsOperation(
                v.findViewById(R.id.topic4),
                v.findViewById(R.id.ageOp4),
                v.findViewById(R.id.goals4),
                v.findViewById(R.id.time4),
                v.findViewById(R.id.organizationOP4),
                v.findViewById(R.id.writerUserName4),
                v.findViewById(R.id.clOp4)
        );

        LayoutViewsTrip layout1ViewsTrip = new LayoutViewsTrip(
                v.findViewById(R.id.title1),
                v.findViewById(R.id.age1),
                v.findViewById(R.id.area1),
                v.findViewById(R.id.length1),
                v.findViewById(R.id.organization1),
                v.findViewById(R.id.id1),
                v.findViewById(R.id.clTr1)
        );
        LayoutViewsTrip layout2ViewsTrip = new LayoutViewsTrip(
                v.findViewById(R.id.title2),
                v.findViewById(R.id.age2),
                v.findViewById(R.id.area2),
                v.findViewById(R.id.length2),
                v.findViewById(R.id.organization2),
                v.findViewById(R.id.id2),
                v.findViewById(R.id.clTr2)
        );
        LayoutViewsTrip layout3ViewsTrip = new LayoutViewsTrip(
                v.findViewById(R.id.title3),
                v.findViewById(R.id.age3),
                v.findViewById(R.id.area3),
                v.findViewById(R.id.length3),
                v.findViewById(R.id.organization3),
                v.findViewById(R.id.id3),
                v.findViewById(R.id.clTr3)
        );
        LayoutViewsTrip layout4ViewsTrip = new LayoutViewsTrip(
                v.findViewById(R.id.title4),
                v.findViewById(R.id.age4),
                v.findViewById(R.id.area4),
                v.findViewById(R.id.length4),
                v.findViewById(R.id.organization4),
                v.findViewById(R.id.id4),
                v.findViewById(R.id.clTr4)
        );

        FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
        fireBaseTripHelper.fetchTrips(
                new FireBaseTripHelper.DataStatus() {
                    @Override
                    public void onDataLoaded(ArrayList<Trip> trips) {
                        ArrayList<Trip> tripArrayList =  new ArrayList<>();
                        for (int i = 0; i<trips.size(); i++){
                            Trip trip = trips.get(i);
                            if (trip.getPublicORprivate().equals("isPublic")) {
                                tripArrayList.add(trip);
                            }
                        }
                        if (!tripArrayList.isEmpty() && tripArrayList.size() >= 4){
                            int index1 = new Random().nextInt(tripArrayList.size());
                            Trip randomTrip = tripArrayList.get(index1);
                            int index2 = new Random().nextInt(tripArrayList.size());
                            while (index2 == index1){
                                index2 = new Random().nextInt(tripArrayList.size());
                            }
                            Trip randomTrip2 = tripArrayList.get(index2);

                            int index3 = new Random().nextInt(tripArrayList.size());
                            while (index3 == index2 || index3 == index1){
                                index3 = new Random().nextInt(tripArrayList.size());
                            }
                            Trip randomTrip3 = tripArrayList.get(index3);

                            int index4 = new Random().nextInt(tripArrayList.size());
                            while (index4 == index2 ||index4 == index1 || index4==index3){
                                index4 = new Random().nextInt(tripArrayList.size());
                            }
                            Trip randomTrip4 = tripArrayList.get(index4);

                            displayTripInLayout(randomTrip, layout1ViewsTrip);
                            displayTripInLayout(randomTrip2, layout2ViewsTrip);
                            displayTripInLayout(randomTrip3, layout3ViewsTrip);
                            displayTripInLayout(randomTrip4, layout4ViewsTrip);

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
                        for (int i = 0; i<operations.size(); i++){
                            Operation operation = operations.get(i);
                            if (operation.getPrivateORpublic().equals("isPublic")) {
                                operationArrayList.add(operation);
                            }
                        }
                        if (!operationArrayList.isEmpty() && operationArrayList.size() >= 4){
                            int index1 = new Random().nextInt(operationArrayList.size());
                            Operation randomOperation = operationArrayList.get(index1);
                            int index2 = new Random().nextInt(operationArrayList.size());
                            while (index2 == index1){
                                index2 = new Random().nextInt(operationArrayList.size());
                            }
                            Operation randomOperation2 = operationArrayList.get(index2);

                            int index3 = new Random().nextInt(operationArrayList.size());
                            while (index3 == index2 || index3 == index1){
                                index3 = new Random().nextInt(operationArrayList.size());
                            }
                            Operation randomOperation3 = operationArrayList.get(index3);

                            int index4 = new Random().nextInt(operationArrayList.size());
                            while (index4 == index2 ||index4 == index1 || index4==index3){
                                index4 = new Random().nextInt(operationArrayList.size());
                            }
                            Operation randomOperation4 = operationArrayList.get(index4);

                            displayOperationInLayout(randomOperation, layout1Views);
                            displayOperationInLayout(randomOperation2, layout2Views);
                            displayOperationInLayout(randomOperation3, layout3Views);
                            displayOperationInLayout(randomOperation4, layout4Views);

                        }
                    }
                }
        );


        return v;
    }

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
    }

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
    }
}