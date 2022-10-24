package com.group9.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.group9.project.R;
import com.group9.project.models.Listing;
import com.group9.project.viewmodels.ListingViewModel;

public class ListingDetailsActivity extends AppCompatActivity {

    private ListingViewModel listingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        listingViewModel = ListingViewModel.getInstance(getApplication());

        ImageView ivListDetailsImage = findViewById(R.id.ivListDetailsImage);
        TextView tvListTitle = findViewById(R.id.tvListTitle);
        TextView tvListDescription = findViewById(R.id.tvListDescription);
        TextView tvListAddress = findViewById(R.id.tvListAddress);
        TextView tvListPrice = findViewById(R.id.tvListPrice);
        AppCompatButton btnListRent = findViewById(R.id.btnListRent);

        Listing listing = getIntent().getParcelableExtra("listing");
        tvListTitle.setText(listing.getTitle());
        tvListDescription.setText("Description: " + listing.getDesc() + "\n\nContact Information\nRenter: " + listing.getUser());
        tvListAddress.setText("Address: " + listing.getAddress());
        tvListPrice.setText("Price: $" + listing.getPrice());
        Glide.with(ivListDetailsImage).load(listing.getImage()).into(ivListDetailsImage);

        btnListRent.setOnClickListener(v -> {
            if (!listing.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                Toast.makeText(this, "You Successfully rented " + listing.getTitle() + " for $" + listing.getPrice() + "!", Toast.LENGTH_SHORT).show();
                listingViewModel.deleteListing(listing.getId());
                finish();
            } else {
                Toast.makeText(this, "You can't rent your own Item!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}