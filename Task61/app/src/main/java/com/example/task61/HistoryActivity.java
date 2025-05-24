package com.example.task61;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61.adapter.QuizHistoryAdapter;
import com.example.task61.model.QuizHistory;
import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;
import com.example.task61.utils.QuizHistoryManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private QuizHistoryManager quizHistoryManager;
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
        quizHistoryManager = new QuizHistoryManager(this);
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
        
        // Load real quiz history data
        loadRealQuizHistory();
    }
    
    private void loadRealQuizHistory() {
        // Simulate loading delay for better UX
        new android.os.Handler().postDelayed(() -> {
            try {
                // Get real quiz history for current user
                List<QuizHistory> userHistory = quizHistoryManager.getQuizHistoryForUser(currentUser.getId());
                
                loadingProgressBar.setVisibility(View.GONE);
                
                if (userHistory.isEmpty()) {
                    emptyStateText.setVisibility(View.VISIBLE);
                    emptyStateText.setText("No quiz history found.\nStart taking quizzes to see your progress!");
                } else {
                    historyRecyclerView.setVisibility(View.VISIBLE);
                    quizHistoryList.clear();
                    quizHistoryList.addAll(userHistory);
                    historyAdapter.notifyDataSetChanged();
                    
                    android.util.Log.d("HistoryActivity", "Loaded " + userHistory.size() + " quiz history entries");
                }
            } catch (Exception e) {
                android.util.Log.e("HistoryActivity", "Error loading quiz history", e);
                loadingProgressBar.setVisibility(View.GONE);
                emptyStateText.setVisibility(View.VISIBLE);
                emptyStateText.setText("Error loading quiz history.\nPlease try again later.");
            }
        }, 500);
    }
} 