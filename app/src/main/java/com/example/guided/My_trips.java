package com.example.guided;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class My_trips extends Fragment implements View.OnClickListener {
    View v;

    ImageView backBTN;

    RecyclerView recyclerView;
    RecyclerMyTripsAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    FireBaseTripHelper fireBaseTripHelper;
    ArrayList<Trip> tripsArrayList = new ArrayList<>();

    public My_trips() {
        // Required empty public constructor
    }


    public static My_trips newInstance(String param1, String param2) {
        My_trips fragment = new My_trips();
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
        v = inflater.inflate(R.layout.fragment_my_trips, container, false);
        if (v != null){

            backBTN = v.findViewById(R.id.back);
            backBTN.setOnClickListener(this);

            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);



            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
                @Override
                public void onUserDataLoaded(User u) {
                    fireBaseTripHelper = new FireBaseTripHelper();
                    fireBaseTripHelper.fetchTrips(
                            new FireBaseTripHelper.DataStatus()
                            {
                                @Override
                                public void onDataLoaded(ArrayList<Trip> trips) {
                                    tripsArrayList = trips;

                                    recyclerAdapter = new RecyclerMyTripsAdapter(tripsArrayList, getContext(), u);
                                    recyclerView.setAdapter(recyclerAdapter);
                                  //  recyclerAdapter.notifyDataSetChanged();


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

    @Override
    public void onClick(View v) {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}