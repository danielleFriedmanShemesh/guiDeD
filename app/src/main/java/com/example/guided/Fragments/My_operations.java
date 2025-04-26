package com.example.guided.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Classes.Operation;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerMyOperationsAdapter;
import com.example.guided.Classes.User;

import java.util.ArrayList;


public class My_operations extends Fragment implements View.OnClickListener {
    View v;

    ImageView backBTN;
    RecyclerView recyclerView;
    RecyclerMyOperationsAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    FireBaseOperationHelper fireBaseOperationHelper;
    ArrayList<Operation> operationArrayList;



    public My_operations() {
        // Required empty public constructor
    }


    public static My_operations newInstance(String param1, String param2) {
        My_operations fragment = new My_operations();
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
        v = inflater.inflate(R.layout.fragment_my_operations, container, false);
        if (v != null){
            //operationArrayList = new ArrayList<>();

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
                    fireBaseOperationHelper = new FireBaseOperationHelper();
                    fireBaseOperationHelper.fetchOperations(
                            new FireBaseOperationHelper.DataStatus()
                            {
                                @Override
                                public void onDataLoaded(ArrayList<Operation> operations) {
                                    operationArrayList = operations;

                                    recyclerAdapter = new RecyclerMyOperationsAdapter(getContext(),operationArrayList, u);
                                    recyclerView.setAdapter(recyclerAdapter);
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