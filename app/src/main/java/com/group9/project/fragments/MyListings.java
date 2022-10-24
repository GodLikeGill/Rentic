package com.group9.project.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.group9.project.R;
import com.group9.project.activities.AddListingActivity;
import com.group9.project.adapters.AllListingAdapter;
import com.group9.project.adapters.MyListingAdapter;
import com.group9.project.models.Listing;
import com.group9.project.viewmodels.ListingViewModel;

import java.util.ArrayList;

public class MyListings extends Fragment {

    MyListingAdapter adapter;
    private ListingViewModel listingViewModel;
    private ArrayList<Listing> myListings  = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnAddList = view.findViewById(R.id.button_more);
        btnAddList.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AddListingActivity.class)));

        listingViewModel = ListingViewModel.getInstance(getActivity().getApplication());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_my_listings);
        adapter = new MyListingAdapter(myListings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        listingViewModel.getAllListing();
        listingViewModel.allListings.observe(this, lists -> {
            myListings.clear();
            if (!lists.isEmpty()){
                for (Listing list : lists) {
                    if (list.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        myListings.add(list);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
}