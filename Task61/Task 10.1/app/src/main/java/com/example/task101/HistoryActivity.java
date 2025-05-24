package com.example.task101;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task101.adapter.QuizHistoryAdapter;
import com.example.task101.model.QuizHistory;
import com.example.task101.model.User;
import com.example.task101.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // UI Components
    private ImageView backButton;
    private TextView titleText;
    private RecyclerView historyRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView emptyStateText;
    
    private QuizHistoryAdapter historyAdapter;
    private List<QuizHistory> quizHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        loadQuizHistory();
    }
    
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        historyRecyclerView = findViewById(R.id.history_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        emptyStateText = findViewById(R.id.empty_state_text);
        
        titleText.setText("Quiz History");
    }
    
    private void setupRecyclerView() {
        quizHistoryList = new ArrayList<>();
        historyAdapter = new QuizHistoryAdapter(this, quizHistoryList);
        
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }
    
    private void loadQuizHistory() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        historyRecyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.GONE);
        
        // Simulate loading quiz history data
        // In a real app, this would be an API call
        loadMockQuizHistory();
    }
    
    private void loadMockQuizHistory() {
        // Create mock quiz history data for demonstration
        List<QuizHistory> mockHistory = new ArrayList<>();
        
        // Quiz 1
        QuizHistory history1 = new QuizHistory(currentUser.getId(), "quiz1", "Java Fundamentals", "Programming");
        history1.setId("history1");
        history1.setTotalQuestions(10);
        history1.setCorrectAnswers(8);
        history1.setIncorrectAnswers(2);
        history1.setAccuracyPercentage(80.0);
        history1.setTimeTaken(300000); // 5 minutes
        history1.setCompletedAt(new Date(System.currentTimeMillis() - 86400000)); // 1 day ago
        
        // Create mock question results
        List<QuizHistory.QuestionResult> results1 = new ArrayList<>();
        results1.add(new QuizHistory.QuestionResult("q1", "What is a class in Java?", 0, 0, true, "A class is a blueprint for objects."));
        results1.add(new QuizHistory.QuestionResult("q2", "What is inheritance?", 1, 0, false, "Inheritance allows a class to inherit properties from another class."));
        history1.setQuestionResults(results1);
        
        // Quiz 2
        QuizHistory history2 = new QuizHistory(currentUser.getId(), "quiz2", "Android Development", "Mobile");
        history2.setId("history2");
        history2.setTotalQuestions(15);
        history2.setCorrectAnswers(12);
        history2.setIncorrectAnswers(3);
        history2.setAccuracyPercentage(80.0);
        history2.setTimeTaken(450000); // 7.5 minutes
        history2.setCompletedAt(new Date(System.currentTimeMillis() - 172800000)); // 2 days ago
        
        // Quiz 3
        QuizHistory history3 = new QuizHistory(currentUser.getId(), "quiz3", "Data Structures", "Computer Science");
        history3.setId("history3");
        history3.setTotalQuestions(12);
        history3.setCorrectAnswers(10);
        history3.setIncorrectAnswers(2);
        history3.setAccuracyPercentage(83.3);
        history3.setTimeTaken(360000); // 6 minutes
        history3.setCompletedAt(new Date(System.currentTimeMillis() - 259200000)); // 3 days ago
        
        mockHistory.addAll(Arrays.asList(history1, history2, history3));
        
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            loadingProgressBar.setVisibility(View.GONE);
            
            if (mockHistory.isEmpty()) {
                emptyStateText.setVisibility(View.VISIBLE);
                emptyStateText.setText("No quiz history found.\nStart taking quizzes to see your progress!");
            } else {
                historyRecyclerView.setVisibility(View.VISIBLE);
                quizHistoryList.clear();
                quizHistoryList.addAll(mockHistory);
                historyAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }
} 