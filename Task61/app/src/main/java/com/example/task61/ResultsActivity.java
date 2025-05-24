package com.example.task61;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.model.QuizHistory;
import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;
import com.example.task61.utils.QuizHistoryManager;

import java.util.Date;

public class ResultsActivity extends AppCompatActivity {

    private TextView resultsTitle;
    private TextView scoreText;
    private TextView question1NumberResult;
    private TextView question1ResultExplanation;
    private TextView question2NumberResult;
    private TextView question2ResultExplanation;
    private TextView question3NumberResult;
    private TextView question3ResultExplanation;
    private Button continueButton;
    
    private SessionManager sessionManager;
    private QuizHistoryManager quizHistoryManager;
    private User currentUser;
    private int correctCount = 0;
    private int totalQuestions = 0;
    private String quizId;
    private String quizTitle;
    private String quizCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        sessionManager = new SessionManager(this);
        quizHistoryManager = new QuizHistoryManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        resultsTitle = findViewById(R.id.resultsTitle);
        scoreText = findViewById(R.id.scoreText);
        question1NumberResult = findViewById(R.id.question1NumberResult);
        question1ResultExplanation = findViewById(R.id.question1ResultExplanation);
        question2NumberResult = findViewById(R.id.question2NumberResult);
        question2ResultExplanation = findViewById(R.id.question2ResultExplanation);
        question3NumberResult = findViewById(R.id.question3NumberResult);
        question3ResultExplanation = findViewById(R.id.question3ResultExplanation);
        continueButton = findViewById(R.id.continueButton);

        // Get quiz results from intent
        Intent intent = getIntent();
        quizTitle = intent.getStringExtra("quiz_title");
        quizId = intent.getStringExtra("quiz_id");
        quizCategory = intent.getStringExtra("quiz_category");
        
        // Update title
        if (quizTitle != null) {
            resultsTitle.setText("Results: " + quizTitle);
        }

        // Display question explanations and calculate score
        processQuestionResult(intent, 1, question1NumberResult, question1ResultExplanation);
        processQuestionResult(intent, 2, question2NumberResult, question2ResultExplanation);
        processQuestionResult(intent, 3, question3NumberResult, question3ResultExplanation);
        
        // Display score
        displayScore();
        
        // Update user statistics
        updateUserStatistics();
        
        // Save quiz to history
        saveQuizHistory();

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
    
    private void processQuestionResult(Intent intent, int questionNumber, TextView resultTextView, TextView explanationTextView) {
        String questionText = intent.getStringExtra("question" + questionNumber + "_text");
        String explanation = intent.getStringExtra("question" + questionNumber + "_explanation");
        boolean isCorrect = intent.getBooleanExtra("question" + questionNumber + "_correct", false);
        
        if (questionText != null && !questionText.isEmpty()) {
            totalQuestions++;
            if (isCorrect) {
                correctCount++;
            }
            
            String resultPrefix = isCorrect ? "✓ " : "✗ ";
            resultTextView.setText(questionNumber + ". " + resultPrefix + questionText);
            resultTextView.setTextColor(isCorrect ? Color.GREEN : Color.RED);
            explanationTextView.setText(explanation != null ? explanation : "Response from the model");
            
            // Make sure the views are visible
            resultTextView.setVisibility(View.VISIBLE);
            explanationTextView.setVisibility(View.VISIBLE);
        } else {
            // Hide question if not available
            resultTextView.setVisibility(View.GONE);
            explanationTextView.setVisibility(View.GONE);
        }
    }
    
    private void displayScore() {
        if (totalQuestions > 0) {
            double percentage = (double) correctCount / totalQuestions * 100;
            scoreText.setText(String.format("Score: %d/%d (%.1f%%)", correctCount, totalQuestions, percentage));
            
            // Set color based on performance
            if (percentage >= 80) {
                scoreText.setTextColor(Color.GREEN);
            } else if (percentage >= 60) {
                scoreText.setTextColor(Color.rgb(255, 165, 0)); // Orange
            } else {
                scoreText.setTextColor(Color.RED);
            }
        } else {
            scoreText.setText("No questions completed");
            scoreText.setTextColor(Color.GRAY);
        }
    }
    
    private void updateUserStatistics() {
        if (totalQuestions > 0) {
            // Log current statistics before update
            android.util.Log.d("ResultsActivity", "Before Update - Total: " + currentUser.getTotalQuestions() 
                + ", Correct: " + currentUser.getCorrectAnswers() 
                + ", Incorrect: " + currentUser.getIncorrectAnswers());
            
            // Update user statistics
            for (int i = 0; i < correctCount; i++) {
                currentUser.incrementCorrectAnswers();
            }
            
            int incorrectCount = totalQuestions - correctCount;
            for (int i = 0; i < incorrectCount; i++) {
                currentUser.incrementIncorrectAnswers();
            }
            
            // Log statistics after update
            android.util.Log.d("ResultsActivity", "After Update - Total: " + currentUser.getTotalQuestions() 
                + ", Correct: " + currentUser.getCorrectAnswers() 
                + ", Incorrect: " + currentUser.getIncorrectAnswers()
                + ", Quiz Score: " + correctCount + "/" + totalQuestions);
            
            // Mark quiz as completed
            if (quizId != null && !currentUser.hasCompletedQuiz(quizId)) {
                if (currentUser.getCompletedQuizIds() == null) {
                    currentUser.setCompletedQuizIds(new java.util.ArrayList<>());
                }
                currentUser.getCompletedQuizIds().add(quizId);
                android.util.Log.d("ResultsActivity", "Marked quiz as completed: " + quizId);
            }
            
            // Save updated user data
            sessionManager.saveUser(currentUser);
            android.util.Log.d("ResultsActivity", "User data saved to session");
            
            Toast.makeText(this, "Quiz completed! Statistics updated.", Toast.LENGTH_SHORT).show();
        } else {
            android.util.Log.w("ResultsActivity", "No questions found to update statistics");
        }
    }
    
    private void saveQuizHistory() {
        if (totalQuestions > 0 && quizTitle != null) {
            try {
                // Create quiz history entry
                QuizHistory history = new QuizHistory();
                history.setId("quiz_" + System.currentTimeMillis());
                history.setUserId(currentUser.getId());
                history.setQuizId(quizId != null ? quizId : "quiz_" + System.currentTimeMillis());
                history.setQuizTitle(quizTitle);
                history.setCategory(quizCategory != null ? quizCategory : "General Knowledge");
                history.setCorrectAnswers(correctCount);
                history.setIncorrectAnswers(totalQuestions - correctCount);
                history.setTotalQuestions(totalQuestions);
                history.setCompletedAt(new Date());
                history.setTimeTaken(0); // Could be calculated if we track time
                
                // Calculate and set accuracy percentage
                double accuracy = totalQuestions > 0 ? (double) correctCount / totalQuestions * 100 : 0.0;
                history.setAccuracyPercentage(accuracy);
                
                // Actually save the quiz history
                quizHistoryManager.saveQuizHistory(history);
                
                android.util.Log.d("QuizHistory", "Quiz history saved: " + history.getQuizTitle() + 
                    " - Score: " + history.getCorrectAnswers() + "/" + history.getTotalQuestions() +
                    " - Accuracy: " + String.format("%.1f%%", accuracy));
                    
            } catch (Exception e) {
                android.util.Log.e("QuizHistory", "Error saving quiz history", e);
            }
        }
    }
} 