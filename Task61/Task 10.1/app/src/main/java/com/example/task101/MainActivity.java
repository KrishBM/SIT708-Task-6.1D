package com.example.task101;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task101.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (sessionManager.isLoggedIn()) {
            // User is logged in, go to dashboard
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // User is not logged in, go to login
            startActivity(new Intent(this, LoginActivity.class));
        }
        
        finish();
    }
} 