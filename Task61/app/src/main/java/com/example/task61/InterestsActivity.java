package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private Chip algorithmsChip1, algorithmsChip2, algorithmsChip3;
    private Chip dataStructuresChip1, dataStructuresChip2, dataStructuresChip3;
    private Chip webDevChip1, webDevChip2;
    private Chip testingChip1, testingChip2;
    private Button nextButton;

    private SessionManager sessionManager;
    private List<String> selectedInterests;
    private static final int MAX_INTERESTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        // Initialize session manager
        sessionManager = new SessionManager(this);
        selectedInterests = new ArrayList<>();

        // Initialize views
        algorithmsChip1 = findViewById(R.id.algorithmsChip1);
        algorithmsChip2 = findViewById(R.id.algorithmsChip2);
        algorithmsChip3 = findViewById(R.id.algorithmsChip3);
        dataStructuresChip1 = findViewById(R.id.dataStructuresChip1);
        dataStructuresChip2 = findViewById(R.id.dataStructuresChip2);
        dataStructuresChip3 = findViewById(R.id.dataStructuresChip3);
        webDevChip1 = findViewById(R.id.webDevChip1);
        webDevChip2 = findViewById(R.id.webDevChip2);
        testingChip1 = findViewById(R.id.testingChip1);
        testingChip2 = findViewById(R.id.testingChip2);
        nextButton = findViewById(R.id.nextButton);

        setupChips();

        // Set click listener for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedInterests.isEmpty()) {
                    Toast.makeText(InterestsActivity.this, "Please select at least one interest", Toast.LENGTH_SHORT).show();
                } else {
                    saveInterestsAndProceed();
                }
            }
        });
    }

    private void setupChips() {
        // Use unique interest identifiers for each chip
        setupChip(algorithmsChip1, "Sorting Algorithms");
        setupChip(algorithmsChip2, "Search Algorithms");
        setupChip(algorithmsChip3, "Graph Algorithms");
        setupChip(dataStructuresChip1, "Arrays and Lists");
        setupChip(dataStructuresChip2, "Trees and Graphs");
        setupChip(dataStructuresChip3, "Hash Tables");
        setupChip(webDevChip1, "Frontend Development");
        setupChip(webDevChip2, "Backend Development");
        setupChip(testingChip1, "Unit Testing");
        setupChip(testingChip2, "Integration Testing");
    }

    private void setupChip(final Chip chip, final String interest) {
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (selectedInterests.size() >= MAX_INTERESTS) {
                        chip.setChecked(false);
                        Toast.makeText(InterestsActivity.this, "You can select maximum 10 topics", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedInterests.add(interest);
                        Toast.makeText(InterestsActivity.this, interest + " selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedInterests.remove(interest);
                }
            }
        });
    }

    private void saveInterestsAndProceed() {
        // Get current user
        User user = sessionManager.getUser();
        
        if (user != null) {
            // Set interests
            user.setInterests(new ArrayList<>(selectedInterests));
            
            // Save updated user
            sessionManager.saveUser(user);
            
            // Navigate to dashboard
            startActivity(new Intent(InterestsActivity.this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            
            // Navigate back to login if there's an issue
            startActivity(new Intent(InterestsActivity.this, LoginActivity.class));
            finish();
        }
    }
} 