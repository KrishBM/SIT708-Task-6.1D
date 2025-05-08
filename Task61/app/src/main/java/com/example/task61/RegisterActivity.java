package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText confirmEmailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneNumberEditText;
    private Button createAccountButton;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameRegisterEditText);
        emailEditText = findViewById(R.id.emailEditText);
        confirmEmailEditText = findViewById(R.id.confirmEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Set click listener for create account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get input values
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String confirmEmail = confirmEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Basic validation
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!email.equals(confirmEmail)) {
            confirmEmailEditText.setError("Email does not match");
            confirmEmailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password does not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Phone number is required");
            phoneNumberEditText.requestFocus();
            return;
        }

        // For the purpose of this demo, we'll just create a new user locally
        // In a real app, you would send this information to an API/database

        // Create user
        User user = new User(UUID.randomUUID().toString(), username, email);

        // Save login status and user details
        sessionManager.setLogin(true);
        sessionManager.saveUser(user);

        // Show success message and navigate to interests selection
        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, InterestsActivity.class));
        finish();
    }
} 