package com.group9.project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group9.project.models.Listing;
import com.group9.project.respositories.ListingRepository;

import java.util.List;

public class ListingViewModel extends AndroidViewModel {

    private final ListingRepository repository = new ListingRepository();
    private static ListingViewModel instance;
    public MutableLiveData<List<Listing>> allListings;

    public ListingViewModel(@NonNull Application application) {
        super(application);
    }

    public static ListingViewModel getInstance(Application application){
        if (instance == null){
            instance = new ListingViewModel(application);
        }
        return instance;
    }

    public ListingRepository getListingRepository(){
        return this.repository;
    }

    public void getAllListing(){
        this.repository.getAllListings();
        this.allListings = this.repository.allListings;
    }

    public void addListing(Listing listing){ this.repository.addListing(listing); }
    public void deleteListing(String id){ this.repository.deleteListing(id);}
}
