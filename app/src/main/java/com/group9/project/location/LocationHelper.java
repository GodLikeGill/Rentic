package com.group9.project.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private final String TAG = this.getClass().getCanonicalName();
    public boolean locationPermissionGranted = false;
    public final int REQUEST_CODE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    MutableLiveData<Location> userLocation = new MutableLiveData<>();
    private LocationRequest locationRequest;

    private static final LocationHelper locationHelper = new LocationHelper();

    public static LocationHelper getInstance() {
        return locationHelper;
    }

    private LocationHelper() {
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(15000);
    }

    public void checkPermissions(Context context) {
        this.locationPermissionGranted =
                (PackageManager.PERMISSION_GRANTED == (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)));
        Log.e(TAG, "checkPermissions: LocationPermissionGranted " + this.locationPermissionGranted);
        if (!this.locationPermissionGranted) {
            requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
        if (this.fusedLocationProviderClient == null) {
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        return this.fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getUserLocation(Context context) {
        if (this.locationPermissionGranted) {
            Log.e(TAG, "getUserLocation: Permission is granted");
            try {
                this.getFusedLocationProviderClient(context)
                        .getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {

                                    userLocation.setValue(location);
                                    Log.e(TAG, "onSuccess: Last location obtained Lat : " + userLocation.getValue().getLatitude() + " Lng : " + userLocation.getValue().getLongitude());
                                } else {
                                    Log.e(TAG, "onSuccess: Unable to access last location");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: Failed to get the last location" + e.getLocalizedMessage());
                            }
                        });
            } catch (Exception ex) {
                Log.e(TAG, "getLastLocation: Exception occurred while fetching last location " + ex.getLocalizedMessage());
                return null;
            }
            return this.userLocation;
        } else {
            Log.e(TAG, "getLastLocation: Location permission not granted");
            requestLocationPermission(context);
            return null;
        }
    }

    public Address performForwardGeocoding(Context context, Location loc) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addressList.size() > 0) {
                Address addressObj = addressList.get(0);

                Log.e(TAG, "performForwardGeocoding: Address " + addressObj.getAddressLine(0));
                Log.e(TAG, "performForwardGeocoding: Postal Code " + addressObj.getPostalCode());
                Log.e(TAG, "performForwardGeocoding: Country code " + addressObj.getCountryCode());
                Log.e(TAG, "performForwardGeocoding: throughfare : " + addressObj.getThoroughfare());
                Log.e(TAG, "performForwardGeocoding: locality " + addressObj.getLocality());

                return addressObj;
            }
        } catch (Exception ex) {
            Log.e(TAG, "performForwardGeocoding: Couldn't get the address for the given location coordinates" + ex.getLocalizedMessage());
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    public void getUserLocationUpdates(Context context, LocationCallback locationCallback) {
        if (locationPermissionGranted) {
            try {
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest, locationCallback, Looper.getMainLooper());
            } catch (Exception ex) {
                Log.e(TAG, "getUserLocationUpdates: " + ex.getLocalizedMessage());
            }
        } else {
            Log.e(TAG, "getUserLocationUpdates: location permission not granted");
        }
    }

}
