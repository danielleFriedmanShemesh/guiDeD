package com.example.guided;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;


public class LibraryTripsFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    RecyclerAdapterLibraryTrip recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    FireBaseTripHelper fireBaseTripHelper;
    ArrayList<Trip> tripsArrayList;
    android.widget.SearchView search;
    ImageButton filter;

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

            search = v.findViewById(R.id.search);




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
                            // TODO: לעשות שהטיול הכי עליון שמוצג זה הטיול האחרון שעלה לאתר
                            //recyclerAdapter.notifyItemRangeInserted(0, tripsArrayList.size());

                            search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
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