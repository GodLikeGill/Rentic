package com.group9.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group9.project.R;
import com.group9.project.models.Listing;
import com.group9.project.viewmodels.ListingViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String nameText, phoneText, emailText, passText;
    EditText name, phone, email, pass;
    AppCompatButton signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextTextEmailAddress1);
        pass = findViewById(R.id.editTextTextPassword1);
        signUp = findViewById(R.id.sign_up1);

        signUp.setOnClickListener(v -> {
            nameText = name.getText().toString();
            phoneText = phone.getText().toString();
            emailText = email.getText().toString();
            passText = pass.getText().toString();

            if (!nameText.isEmpty() || !phoneText.isEmpty() || !emailText.isEmpty() || !passText.isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText, passText).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, String> userData = new HashMap<>();
                        userData.put("name", nameText);
                        userData.put("phone", phoneText);
                        userData.put("email", emailText);

                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(userData).addOnSuccessListener(aVoid -> {


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}