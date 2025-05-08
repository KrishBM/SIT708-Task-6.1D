package com.example.task61;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView resultsTitle;
    private TextView question1NumberResult;
    private TextView question1ResultExplanation;
    private TextView question2NumberResult;
    private TextView question2ResultExplanation;
    private TextView question3NumberResult;
    private TextView question3ResultExplanation;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Initialize views
        resultsTitle = findViewById(R.id.resultsTitle);
        question1NumberResult = findViewById(R.id.question1NumberResult);
        question1ResultExplanation = findViewById(R.id.question1ResultExplanation);
        question2NumberResult = findViewById(R.id.question2NumberResult);
        question2ResultExplanation = findViewById(R.id.question2ResultExplanation);
        question3NumberResult = findViewById(R.id.question3NumberResult);
        question3ResultExplanation = findViewById(R.id.question3ResultExplanation);
        continueButton = findViewById(R.id.continueButton);

        // Get quiz results from intent
        Intent intent = getIntent();
        String quizTitle = intent.getStringExtra("quiz_title");
        
        // Update title
        if (quizTitle != null) {
            resultsTitle.setText("Results: " + quizTitle);
        }

        // Display question explanations
        String q1Text = intent.getStringExtra("question1_text");
        String q1Explanation = intent.getStringExtra("question1_explanation");
        boolean q1Correct = intent.getBooleanExtra("question1_correct", false);
        
        if (q1Text != null) {
            String resultPrefix = q1Correct ? "✓ " : "✗ ";
            question1NumberResult.setText("1. " + resultPrefix + q1Text);
            question1NumberResult.setTextColor(q1Correct ? Color.GREEN : Color.RED);
            question1ResultExplanation.setText(q1Explanation != null ? q1Explanation : "Response from the model");
        }

        String q2Text = intent.getStringExtra("question2_text");
        String q2Explanation = intent.getStringExtra("question2_explanation");
        boolean q2Correct = intent.getBooleanExtra("question2_correct", false);
        
        if (q2Text != null) {
            String resultPrefix = q2Correct ? "✓ " : "✗ ";
            question2NumberResult.setText("2. " + resultPrefix + q2Text);
            question2NumberResult.setTextColor(q2Correct ? Color.GREEN : Color.RED);
            question2ResultExplanation.setText(q2Explanation != null ? q2Explanation : "Response from the model");
        } else {
            // Hide second question if not available
            question2NumberResult.setVisibility(View.GONE);
            question2ResultExplanation.setVisibility(View.GONE);
        }

        String q3Text = intent.getStringExtra("question3_text");
        String q3Explanation = intent.getStringExtra("question3_explanation");
        boolean q3Correct = intent.getBooleanExtra("question3_correct", false);
        
        if (q3Text != null) {
            String resultPrefix = q3Correct ? "✓ " : "✗ ";
            question3NumberResult.setText("3. " + resultPrefix + q3Text);
            question3NumberResult.setTextColor(q3Correct ? Color.GREEN : Color.RED);
            question3ResultExplanation.setText(q3Explanation != null ? q3Explanation : "Response from the model");
        } else {
            // Hide third question if not available
            question3NumberResult.setVisibility(View.GONE);
            question3ResultExplanation.setVisibility(View.GONE);
        }

        // Set continue button click listener
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to dashboard
                Intent dashboardIntent = new Intent(ResultsActivity.this, DashboardActivity.class);
                dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
                startActivity(dashboardIntent);
                finish();
            }
        });
    }
} 