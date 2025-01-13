package com.example.guided;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LibraryTripsFragment extends Fragment {
    View v;

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
        v= inflater.inflate(R.layout.fragment_library_trips, container, false);
        return v;
    }
}