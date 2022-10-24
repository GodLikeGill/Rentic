package com.group9.project.respositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group9.project.models.Listing;
import com.group9.project.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingRepository {

    private final FirebaseFirestore db;
    private final String COLLECTION_LISTINGS = "Listings";
    private final String FIELD_ID = "id";
    private final String FIELD_USER = "user";
    private final String FIELD_TITLE = "title";
    private final String FIELD_DESC = "desc";
    private final String FIELD_ADDRESS = "address";
    private final String FIELD_PRICE = "price";
    private final String FIELD_IMAGE = "image";
    public MutableLiveData<List<Listing>> allListings = new MutableLiveData<>();

    public ListingRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllListings() {
        try {
            db.collection(COLLECTION_LISTINGS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Listing> listingsList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllListings: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Listing listing = documentChange.getDocument().toObject(Listing.class);
                        listingsList.add(listing);
                        Log.d("TAG", "getAllListings: " + listing.getAddress());
                    }
                }
                allListings.postValue(listingsList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllListings: " + e.getLocalizedMessage());
        }
    }

    public void addListing(Listing listing) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_ID, listing.getId());
            data.put(FIELD_USER, listing.getUser());
            data.put(FIELD_TITLE, listing.getTitle());
            data.put(FIELD_DESC, listing.getDesc());
            data.put(FIELD_ADDRESS, listing.getAddress());
            data.put(FIELD_PRICE, listing.getPrice());
            data.put(FIELD_IMAGE, listing.getImage());
            db.collection(COLLECTION_LISTINGS).document(listing.getId()).set(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addListing: Listing created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating document " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addListing: " + e.getLocalizedMessage());
        }
    }

    public void deleteListing(String id) {
        try {
            db.collection(COLLECTION_LISTINGS).document(id).delete()
                    .addOnSuccessListener(unused -> Log.d("TAG", "deleteListing: Successfully deleted listing"))
                    .addOnFailureListener(e -> Log.e("TAG", "deleteListing: " + e.getLocalizedMessage()));
        } catch (Exception e) {
            Log.e("TAG", "deleteListing: " + e.getLocalizedMessage());
        }
    }
}
