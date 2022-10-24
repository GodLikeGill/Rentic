package com.group9.project.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.group9.project.R;
import com.group9.project.activities.LoginActivity;
import com.group9.project.adapters.AllListingAdapter;
import com.group9.project.models.Listing;
import com.group9.project.viewmodels.ListingViewModel;

import java.util.ArrayList;

public class AllListings extends Fragment {

    AllListingAdapter adapter;
    private ListingViewModel listingViewModel;
    private ArrayList<Listing> listings  = new ArrayList<>();

    public AllListings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AllListingAdapter(listings);
        listingViewModel = ListingViewModel.getInstance(getActivity().getApplication());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_all_listings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        view.findViewById(R.id.tvLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        listingViewModel.getAllListing();
        listingViewModel.allListings.observe(this, lists -> {
            listings.clear();
            if (!lists.isEmpty())
                listings.addAll(lists);
            adapter.notifyDataSetChanged();
        });
    }
}