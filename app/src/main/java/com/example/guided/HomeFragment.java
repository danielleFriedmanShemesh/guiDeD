package com.example.guided;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        return v;
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
}