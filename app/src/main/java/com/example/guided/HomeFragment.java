package com.example.guided;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class HomeFragment extends Fragment implements View.OnClickListener {
    View v;
    ConstraintLayout see_more_operation;
    ConstraintLayout constraintLayout;
    ConstraintLayout clOp1;
    ConstraintLayout clOp2;
    ConstraintLayout clOp3;
    ConstraintLayout clOp4;
    ConstraintLayout see_more_trip;
    ConstraintLayout clTr1;
    ConstraintLayout clTr2;
    ConstraintLayout clTr3;
    ConstraintLayout clTr4;




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

        LayoutViews layout1Views = new LayoutViews(
                v.findViewById(R.id.topic1),
                v.findViewById(R.id.ageOp1),
                v.findViewById(R.id.goals1),
                v.findViewById(R.id.time1),
                v.findViewById(R.id.organizationOP1),
                v.findViewById(R.id.writerUserName1),
                v.findViewById(R.id.clOp1)
        );
        LayoutViews layout2Views = new LayoutViews(
                v.findViewById(R.id.topic2),
                v.findViewById(R.id.ageOp2),
                v.findViewById(R.id.goals2),
                v.findViewById(R.id.time2),
                v.findViewById(R.id.organizationOP2),
                v.findViewById(R.id.writerUserName2),
                v.findViewById(R.id.clOp2)
        );
        LayoutViews layout3Views = new LayoutViews(
                v.findViewById(R.id.topic3),
                v.findViewById(R.id.ageOp3),
                v.findViewById(R.id.goals3),
                v.findViewById(R.id.time3),
                v.findViewById(R.id.organizationOP3),
                v.findViewById(R.id.writerUserName3),
                v.findViewById(R.id.clOp3)
        );
        LayoutViews layout4Views = new LayoutViews(
                v.findViewById(R.id.topic4),
                v.findViewById(R.id.ageOp4),
                v.findViewById(R.id.goals4),
                v.findViewById(R.id.time4),
                v.findViewById(R.id.organizationOP4),
                v.findViewById(R.id.writerUserName4),
                v.findViewById(R.id.clOp4)
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

    private void displayOperationInLayout(Operation randomOperation, LayoutViews views) {
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

    private class LayoutViews {
        TextView topic;
        TextView time;
        TextView age;
        TextView goals;
        TextView userName;
        TextView organization;
        ConstraintLayout parentLayout;

        public LayoutViews(TextView topic, TextView age, TextView goals,TextView time,TextView organization, TextView userName,ConstraintLayout parentLayout) {
            this.age = age;
            this.parentLayout = parentLayout;
            this.goals = goals;
            this.time = time;
            this.organization = organization;
            this.topic = topic;
            this.userName = userName;
        }
    }
}