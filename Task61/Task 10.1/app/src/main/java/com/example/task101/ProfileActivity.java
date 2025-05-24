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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // UI Components
    private ImageView profileImage;
    private TextView usernameText;
    private TextView emailText;
    private TextView memberSinceText;
    private TextView subscriptionTierText;
    private TextView totalQuestionsText;
    private TextView correctAnswersText;
    private TextView incorrectAnswersText;
    private TextView accuracyText;
    private PieChart accuracyChart;
    private Button shareProfileBtn;
    private Button upgradeAccountBtn;
    private Button viewHistoryBtn;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
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
        populateUserData();
        setupAccuracyChart();
    }
    
    private void initializeViews() {
        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        memberSinceText = findViewById(R.id.member_since_text);
        subscriptionTierText = findViewById(R.id.subscription_tier_text);
        totalQuestionsText = findViewById(R.id.total_questions_text);
        correctAnswersText = findViewById(R.id.correct_answers_text);
        incorrectAnswersText = findViewById(R.id.incorrect_answers_text);
        accuracyText = findViewById(R.id.accuracy_text);
        accuracyChart = findViewById(R.id.accuracy_chart);
        shareProfileBtn = findViewById(R.id.share_profile_btn);
        upgradeAccountBtn = findViewById(R.id.upgrade_account_btn);
        viewHistoryBtn = findViewById(R.id.view_history_btn);
        backButton = findViewById(R.id.back_button);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        shareProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShareProfileActivity.class);
            startActivity(intent);
        });
        
        upgradeAccountBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
            startActivity(intent);
        });
        
        viewHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
    
    private void populateUserData() {
        usernameText.setText(currentUser.getUsername());
        emailText.setText(currentUser.getEmail());
        
        // Format registration date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        memberSinceText.setText("Member since " + dateFormat.format(currentUser.getRegistrationDate()));
        
        // Subscription tier
        subscriptionTierText.setText(currentUser.getSubscriptionTier().getDisplayName());
        
        // Statistics
        totalQuestionsText.setText(String.valueOf(currentUser.getTotalQuestions()));
        correctAnswersText.setText(String.valueOf(currentUser.getCorrectAnswers()));
        incorrectAnswersText.setText(String.valueOf(currentUser.getIncorrectAnswers()));
        
        // Accuracy percentage
        double accuracy = currentUser.getAccuracyPercentage();
        accuracyText.setText(String.format(Locale.getDefault(), "%.1f%%", accuracy));
        
        // Hide upgrade button if already on highest tier
        if (currentUser.getSubscriptionTier() == User.SubscriptionTier.ADVANCED) {
            upgradeAccountBtn.setVisibility(View.GONE);
        }
    }
    
    private void setupAccuracyChart() {
        List<PieEntry> entries = new ArrayList<>();
        
        if (currentUser.getTotalQuestions() > 0) {
            entries.add(new PieEntry(currentUser.getCorrectAnswers(), "Correct"));
            entries.add(new PieEntry(currentUser.getIncorrectAnswers(), "Incorrect"));
        } else {
            entries.add(new PieEntry(1, "No data"));
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "Quiz Performance");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        
        PieData data = new PieData(dataSet);
        accuracyChart.setData(data);
        accuracyChart.getDescription().setEnabled(false);
        accuracyChart.setDrawHoleEnabled(true);
        accuracyChart.setHoleRadius(40f);
        accuracyChart.setTransparentCircleRadius(45f);
        accuracyChart.invalidate();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data in case it was updated
        currentUser = sessionManager.getCurrentUser();
        if (currentUser != null) {
            populateUserData();
            setupAccuracyChart();
        }
    }
} 