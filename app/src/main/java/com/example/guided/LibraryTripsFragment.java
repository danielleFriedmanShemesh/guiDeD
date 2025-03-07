package com.example.guided;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class LibraryTripsFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    RecyclerAdapterLibraryTrip recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    FireBaseTripHelper fireBaseTripHelper;
    ArrayList<Trip> tripsArrayList;

    public LibraryTripsFragment() {
        // Required empty public constructor
    }


    public static LibraryTripsFragment newInstance(String param1, String param2) {
        LibraryTripsFragment fragment = new LibraryTripsFragment();
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
        v = inflater.inflate(R.layout.fragment_library_trips, container, false);

        if ( v != null)
        {
            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            fireBaseTripHelper = new FireBaseTripHelper();
            fireBaseTripHelper.fetchTrips(
                    new FireBaseTripHelper.DataStatus()
                    {
                        @Override
                        public void onDataLoaded(ArrayList<Trip> trips) {
                            tripsArrayList = trips;
                            recyclerAdapter = new RecyclerAdapterLibraryTrip(getContext(), tripsArrayList);
                            recyclerView.setAdapter(recyclerAdapter);
                            //recyclerAdapter.notifyItemRangeInserted(0, tripsArrayList.size());
                        }
                    }
            );
        }

        return v;
    }
}