package com.example.task101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.task101.model.User;
import com.example.task101.utils.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // UI Components
    private TextView welcomeText;
    private TextView userStatsText;
    private CardView profileCard;
    private CardView historyCard;
    private CardView upgradeCard;
    private CardView shareCard;
    private CardView quizCard;
    private CardView interestsCard;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        initializeViews();
        setupClickListeners();
        updateUI();
    }
    
    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        userStatsText = findViewById(R.id.user_stats_text);
        profileCard = findViewById(R.id.profile_card);
        historyCard = findViewById(R.id.history_card);
        upgradeCard = findViewById(R.id.upgrade_card);
        shareCard = findViewById(R.id.share_card);
        quizCard = findViewById(R.id.quiz_card);
        interestsCard = findViewById(R.id.interests_card);
        logoutButton = findViewById(R.id.logout_button);
    }
    
    private void setupClickListeners() {
        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        
        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        
        upgradeCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UpgradeActivity.class);
            startActivity(intent);
        });
        
        shareCard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ShareProfileActivity.class);
            startActivity(intent);
        });
        
        quizCard.setOnClickListener(v -> {
            // Navigate to quiz activity (would be implemented)
            Toast.makeText(this, "Quiz feature - Coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        interestsCard.setOnClickListener(v -> {
            // Navigate to interests activity (would be implemented)
            Toast.makeText(this, "Interests management - Coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        logoutButton.setOnClickListener(v -> logout());
    }
    
    private void updateUI() {
        welcomeText.setText("Welcome back, " + currentUser.getUsername() + "!");
        
        // Update user stats
        String stats = String.format("📊 %d Questions • %.1f%% Accuracy • %s Member",
                currentUser.getTotalQuestions(),
                currentUser.getAccuracyPercentage(),
                currentUser.getSubscriptionTier().getDisplayName());
        userStatsText.setText(stats);
        
        // Hide upgrade card if user is already on highest tier
        if (currentUser.getSubscriptionTier() == User.SubscriptionTier.ADVANCED) {
            upgradeCard.setVisibility(View.GONE);
        }
    }
    
    private void logout() {
        sessionManager.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data in case it was updated
        currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }
} 