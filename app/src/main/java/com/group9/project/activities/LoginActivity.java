package com.group9.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group9.project.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvRegister = findViewById(R.id.tv_dont_have_account);
        EditText email = findViewById(R.id.editTextEmailAddress);
        EditText pass = findViewById(R.id.editTextPassword);
        AppCompatButton signIn = findViewById(R.id.sign_in1);

        signIn.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passText = pass.getText().toString();
            if (!emailText.isEmpty() || !passText.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, passText).addOnSuccessListener(authResult -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}