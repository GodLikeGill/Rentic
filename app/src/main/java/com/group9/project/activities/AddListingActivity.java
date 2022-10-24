package com.group9.project.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group9.project.R;
import com.group9.project.location.LocationHelper;
import com.group9.project.models.Listing;
import com.group9.project.viewmodels.ListingViewModel;

import java.io.IOException;
import java.util.UUID;

public class AddListingActivity extends AppCompatActivity {

    private static final int GalleryPick = 1;
    private ListingViewModel listingViewModel;
    private LocationHelper locationHelper;
    private Location userLocation;
    private LocationCallback locationCallback;
    private String imageUUID;
    private Uri imageUri;
    ImageView ivListImage;
    private StorageReference storageReference;
    Listing newListing = new Listing();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        locationHelper = LocationHelper.getInstance();
        locationHelper.checkPermissions(this);

        listingViewModel = ListingViewModel.getInstance(getApplication());

        ivListImage = findViewById(R.id.ivListImage);
        ImageButton iBtnAddImage = findViewById(R.id.iBtnAddUpdateListImage);
        EditText etListTitle = findViewById(R.id.etListTitle);
        EditText etListDescription = findViewById(R.id.etListDescription);
        EditText etAddress = findViewById(R.id.etListAddress);
        EditText etPrice = findViewById(R.id.etListPrice);
        AppCompatButton btnAddListing = findViewById(R.id.btnAddList);

        iBtnAddImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(AddListingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        this.locationHelper.getUserLocation(this).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null) {
                    userLocation = location;

                    Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), userLocation);

                    if (obtainedAddress != null) {
                        etAddress.setText(obtainedAddress.getAddressLine(0));
                    } else {
                        etAddress.setText(userLocation.toString());
                    }
                } else {
                    etAddress.setText("Last location not obtained");
                }
            }
        });

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations()) {
                    userLocation = loc;
                    Log.e("abc", "onLocationResult: updated location : " + userLocation.toString());
                    Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), userLocation);
                    if (obtainedAddress != null) {
                        etAddress.setText(obtainedAddress.getAddressLine(0));
                    } else {
                        etAddress.setText(userLocation.toString());
                    }
                }
            }
        };
        this.locationHelper.getUserLocationUpdates(this, locationCallback);

        btnAddListing.setOnClickListener(view -> {
            if (etListTitle.getText().toString().isEmpty() || etAddress.getText().toString().isEmpty() || etListDescription.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty()) {
                Toast.makeText(AddListingActivity.this, "All details should be Filled ", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
                newListing.setTitle(etListTitle.getText().toString());
                newListing.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                newListing.setAddress(etAddress.getText().toString());
                newListing.setDesc(etListDescription.getText().toString());
                newListing.setPrice(etPrice.getText().toString());
                newListing.setId(UUID.randomUUID().toString());
                listingViewModel.addListing(newListing);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivListImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);
        } else {
            Toast.makeText(AddListingActivity.this, "Please allow storage permission to add photos", Toast.LENGTH_SHORT).show();
        }
    });

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(this, "You must select product photo.", Toast.LENGTH_SHORT).show();
            return;
        }
        imageUUID = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference("images/" + imageUUID);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

            ivListImage.setImageURI(null);
            Log.d("TAG", "uploadImage: Successfully uploaded images to storage");

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    newListing.setImage(uri.toString());
                    listingViewModel.addListing(newListing);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "uploadImage: Error uploading images to storage " + e.getLocalizedMessage());
                }
            });

        }).addOnFailureListener(e -> {
            Log.e("TAG", "uploadImage: Error uploading images to storage " + e.getLocalizedMessage());
        });
    }
}