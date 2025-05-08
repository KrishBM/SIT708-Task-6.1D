package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.task61.adapter.TaskAdapter;
import com.example.task61.api.LLMApiService;
import com.example.task61.model.Quiz;
import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView greetingText;
    private TextView userNameText;
    private TextView taskCountText;
    private ImageView profileImageView;
    private RecyclerView tasksRecyclerView;
    private Button signOutButton;

    private SessionManager sessionManager;
    private TaskAdapter taskAdapter;
    private List<Quiz> quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize views
        greetingText = findViewById(R.id.greetingText);
        userNameText = findViewById(R.id.userNameText);
        taskCountText = findViewById(R.id.taskCountText);
        profileImageView = findViewById(R.id.profileImageView);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        signOutButton = findViewById(R.id.signOutButton);

        // Set up sign out button
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // Setup RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizzes = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, quizzes);
        tasksRecyclerView.setAdapter(taskAdapter);

        // Load user information
        loadUserInfo();

        // Generate personalized quizzes based on user interests
        generateQuizzes();
    }

    private void signOut() {
        // Show confirmation toast
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
        
        // Clear user session
        sessionManager.logout();
        
        // Navigate to login screen
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loadUserInfo() {
        User user = sessionManager.getUser();
        if (user != null) {
            userNameText.setText(user.getUsername());
            
            // Load profile image if available
            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(user.getProfileImageUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(profileImageView);
            }
        }
    }

    private void generateQuizzes() {
        User user = sessionManager.getUser();
        
        // Generate quizzes based on user interests
        if (user != null && user.getInterests() != null && !user.getInterests().isEmpty()) {
            // For the demo, we'll create quizzes for each interest
            // In a real app, you would use the LLM API to generate these quizzes
            List<String> interests = user.getInterests();
            
            // Clear existing quizzes
            quizzes.clear();
            
            // For each unique interest, generate a quiz
            List<String> processedInterests = new ArrayList<>();
            for (String interest : interests) {
                if (!processedInterests.contains(interest)) {
                    processedInterests.add(interest);
                    generateQuizForInterest(interest);
                }
            }
            
            // Update task count with incomplete tasks after a short delay to allow quizzes to load
            tasksRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    updateTaskCount();
                }
            });
        } else {
            // No interests, create a default quiz
            LLMApiService apiService = new LLMApiService(this);
            apiService.generateQuiz("General Knowledge", new LLMApiService.QuizGenerationCallback() {
                @Override
                public void onSuccess(Quiz quiz) {
                    quizzes.add(quiz);
                    taskAdapter.notifyDataSetChanged();
                    updateTaskCount();
                }

                @Override
                public void onError(String errorMessage) {
                    // Handle error
                    taskCountText.setText("You have 0 tasks due");
                }
            });
        }
    }

    /**
     * Updates the task count text based on incomplete quizzes
     */
    private void updateTaskCount() {
        // Count incomplete quizzes
        int incompleteTasks = 0;
        User user = sessionManager.getUser();
        
        if (user != null && user.getCompletedQuizIds() != null) {
            for (Quiz quiz : quizzes) {
                if (!user.getCompletedQuizIds().contains(quiz.getId())) {
                    incompleteTasks++;
                }
            }
        } else {
            incompleteTasks = quizzes.size();
        }
        
        // Update the task count text
        taskCountText.setText("You have " + incompleteTasks + " " + 
                (incompleteTasks == 1 ? "task" : "tasks") + " due");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh dashboard when returning to this screen
        refreshDashboard();
    }
    
    /**
     * Refreshes the dashboard display with current data
     */
    private void refreshDashboard() {
        if (taskAdapter != null && quizzes != null) {
            // Notify adapter to refresh its views
            taskAdapter.notifyDataSetChanged();
            
            // Update task count
            updateTaskCount();
            
            // Load latest user info
            loadUserInfo();
        }
    }

    private void generateQuizForInterest(String interest) {
        LLMApiService apiService = new LLMApiService(this);
        apiService.generateQuiz(interest, new LLMApiService.QuizGenerationCallback() {
            @Override
            public void onSuccess(Quiz quiz) {
                quizzes.add(quiz);
                taskAdapter.notifyDataSetChanged();
                updateTaskCount();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error, could show a toast or retry
            }
        });
    }
} 