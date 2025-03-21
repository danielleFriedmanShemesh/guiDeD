package com.example.guided;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;


public class LibraryOperationsFragment extends Fragment {
    View v;

    RecyclerView recyclerView;
    RecyclerAdapterLibraryOperation recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    FireBaseOperationHelper fireBaseOperationHelper;
    ArrayList<Operation> operationArrayList;

    android.widget.SearchView search;
    ImageView filter;


    public LibraryOperationsFragment() {
        // Required empty public constructor
    }


    public static LibraryOperationsFragment newInstance(String param1, String param2) {
        LibraryOperationsFragment fragment = new LibraryOperationsFragment();
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
        v = inflater.inflate(R.layout.fragment_library_operations, container, false);
        if ( v != null)
        {

            search = v.findViewById(R.id.search);
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

            recyclerView = v.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);


            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            fireBaseOperationHelper = new FireBaseOperationHelper();
            fireBaseOperationHelper.fetchOperations(
                    new FireBaseOperationHelper.DataStatus()
                    {
                    @Override
                    public void onDataLoaded(ArrayList<Operation> operations) {
                        operationArrayList = operations;
                        recyclerAdapter = new RecyclerAdapterLibraryOperation(getContext(), operationArrayList);
                        recyclerView.setAdapter(recyclerAdapter);
                        //recyclerAdapter.notifyItemRangeInserted(0, operationArrayList.size());
                    }
                }
            );
        }

        return v;
    }
}