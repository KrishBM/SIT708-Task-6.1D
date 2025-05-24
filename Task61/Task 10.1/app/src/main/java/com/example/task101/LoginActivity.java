package com.example.task101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task101.model.User;
import com.example.task101.utils.SessionManager;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerLinkText;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerLinkText = findViewById(R.id.register_link_text);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        
        registerLinkText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Simulate login process
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // In a real app, this would be an API call
        simulateLogin(email, password);
    }

    private void simulateLogin(String email, String password) {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            // Create a demo user for login
            User user = new User("user123", "DemoUser", email);
            
            // Add some demo data
            user.addInterest("Programming");
            user.addInterest("Android Development");
            user.addInterest("Machine Learning");
            
            // Add some demo statistics
            user.setTotalQuestions(25);
            user.setCorrectAnswers(20);
            user.setIncorrectAnswers(5);
            user.setSubscriptionTier(User.SubscriptionTier.STARTER);

            // Create login session
            sessionManager.createLoginSession(user);

            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Navigate to dashboard
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();

        }, 1500);
    }
} 