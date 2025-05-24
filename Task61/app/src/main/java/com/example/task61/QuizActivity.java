package com.example.task61;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.api.LLMApiService;
import com.example.task61.model.Question;
import com.example.task61.model.Quiz;
import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private TextView quizTitle;
    private TextView quizDescription;
    private Button submitButton;
    
    private ImageView question1StatusIcon;
    private ImageView question2StatusIcon;
    private ImageView question3StatusIcon;

    private String quizId;
    private String quizTitleString;
    private String quizDescriptionString;
    private String quizCategory;
    private Quiz currentQuiz;
    private SessionManager sessionManager;

    private Map<String, Integer> userAnswers = new HashMap<>();
    private List<RadioGroup> questionRadioGroups = new ArrayList<>();
    private List<ImageView> questionStatusIcons = new ArrayList<>();
    private boolean[] correctAnswers = new boolean[3];
    private boolean quizSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize views
        quizTitle = findViewById(R.id.quizTitle);
        quizDescription = findViewById(R.id.quizDescription);
        submitButton = findViewById(R.id.submitButton);
        
        question1StatusIcon = findViewById(R.id.question1StatusIcon);
        question2StatusIcon = findViewById(R.id.question2StatusIcon);
        question3StatusIcon = findViewById(R.id.question3StatusIcon);
        
        // Add status icons to list
        questionStatusIcons.add(question1StatusIcon);
        questionStatusIcons.add(question2StatusIcon);
        questionStatusIcons.add(question3StatusIcon);

        // Get quiz details from intent
        Intent intent = getIntent();
        quizId = intent.getStringExtra("quiz_id");
        quizTitleString = intent.getStringExtra("quiz_title");
        quizDescriptionString = intent.getStringExtra("quiz_description");
        quizCategory = intent.getStringExtra("quiz_category");

        // Set quiz title and description
        quizTitle.setText(quizTitleString);
        quizDescription.setText(quizDescriptionString);

        // Load quiz content
        loadQuizContent();

        // Set submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quizSubmitted) {
                    checkAnswersAndShowResults();
                } else {
                    if (allAnswersCorrect()) {
                        // Quiz is complete - go back to dashboard
                        Toast.makeText(QuizActivity.this, "Skill Achieved! Congratulations!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        // User needs to retry incorrect answers
                        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                        intent.putExtra("quiz_id", quizId);
                        intent.putExtra("quiz_title", quizTitleString);
                        intent.putExtra("quiz_category", quizCategory);
                        
                        // Pass each question's data and result
                        List<Question> questions = currentQuiz.getQuestions();
                        for (int i = 0; i < questions.size() && i < 3; i++) {
                            Question q = questions.get(i);
                            intent.putExtra("question" + (i+1) + "_text", q.getQuestionText());
                            intent.putExtra("question" + (i+1) + "_explanation", q.getExplanation());
                            intent.putExtra("question" + (i+1) + "_correct", correctAnswers[i]);
                        }
                        
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void loadQuizContent() {
        if (quizId == null || quizCategory == null) {
            Toast.makeText(this, "Error loading quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check if user has already completed this quiz
        User user = sessionManager.getCurrentUser();
        if (user != null && user.getCompletedQuizIds() != null && user.getCompletedQuizIds().contains(quizId)) {
            Toast.makeText(this, "You've already completed this quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // In a real app, you would fetch the quiz from a database or API
        // based on the quiz ID. For this demo, we'll generate a new quiz.
        LLMApiService apiService = new LLMApiService(this);
        apiService.generateQuiz(quizCategory, new LLMApiService.QuizGenerationCallback() {
            @Override
            public void onSuccess(Quiz quiz) {
                currentQuiz = quiz;
                displayQuizQuestions(quiz);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(QuizActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                // Create a dummy quiz for demo purposes
                currentQuiz = new Quiz(quizId, quizTitleString, quizDescriptionString, quizCategory);
                Question q1 = new Question("q1", "Sample question about " + quizCategory + "?", 
                        List.of("Option A", "Option B", "Option C"), 0, "This is the explanation");
                Question q2 = new Question("q2", "Another question about " + quizCategory + "?", 
                        List.of("Option X", "Option Y", "Option Z"), 1, "This is the explanation");
                Question q3 = new Question("q3", "Third question about " + quizCategory + "?", 
                        List.of("Option P", "Option Q", "Option R"), 2, "This is the explanation");
                currentQuiz.addQuestion(q1);
                currentQuiz.addQuestion(q2);
                currentQuiz.addQuestion(q3);
                displayQuizQuestions(currentQuiz);
            }
        });
    }

    private void displayQuizQuestions(Quiz quiz) {
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear previous radio groups
        questionRadioGroups.clear();

        // In a real app, you would dynamically create UI elements for each question
        // For this demo, we'll just populate the existing UI elements with the three questions
        
        List<Question> questions = quiz.getQuestions();
        
        if (questions.size() >= 1) {
            Question q1 = questions.get(0);
            TextView q1Number = findViewById(R.id.question1Number);
            TextView q1Text = findViewById(R.id.question1Text);
            RadioGroup q1Options = findViewById(R.id.question1Options);
            
            q1Number.setText("1.");
            q1Text.setText(q1.getQuestionText());
            
            // Clear previous options
            q1Options.removeAllViews();
            
            // Add new options
            List<String> options = q1.getOptions();
            for (int i = 0; i < options.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(options.get(i));
                radioButton.setId(View.generateViewId());
                q1Options.addView(radioButton);
            }
            
            questionRadioGroups.add(q1Options);
        }
        
        if (questions.size() >= 2) {
            Question q2 = questions.get(1);
            TextView q2Number = findViewById(R.id.question2Number);
            TextView q2Text = findViewById(R.id.question2Text);
            RadioGroup q2Options = findViewById(R.id.question2Options);
            
            q2Number.setText("2.");
            q2Text.setText(q2.getQuestionText());
            
            // Clear previous options
            q2Options.removeAllViews();
            
            // Add new options
            List<String> options = q2.getOptions();
            for (int i = 0; i < options.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(options.get(i));
                radioButton.setId(View.generateViewId());
                q2Options.addView(radioButton);
            }
            
            questionRadioGroups.add(q2Options);
        }
        
        if (questions.size() >= 3) {
            Question q3 = questions.get(2);
            TextView q3Number = findViewById(R.id.question3Number);
            TextView q3Text = findViewById(R.id.question3Text);
            RadioGroup q3Options = findViewById(R.id.question3Options);
            
            q3Number.setText("3.");
            q3Text.setText(q3.getQuestionText());
            
            // Clear previous options
            q3Options.removeAllViews();
            
            // Add new options
            List<String> options = q3.getOptions();
            for (int i = 0; i < options.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(options.get(i));
                radioButton.setId(View.generateViewId());
                q3Options.addView(radioButton);
            }
            
            questionRadioGroups.add(q3Options);
        }
    }

    private void checkAnswersAndShowResults() {
        // Check if all questions are answered
        if (currentQuiz == null || currentQuiz.getQuestions() == null) {
            Toast.makeText(this, "Quiz not loaded properly", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect user answers
        userAnswers.clear();
        boolean allAnswered = true;
        
        for (int i = 0; i < questionRadioGroups.size(); i++) {
            RadioGroup radioGroup = questionRadioGroups.get(i);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            
            if (selectedId == -1) {
                allAnswered = false;
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                break;
            }
            
            // Find the index of the selected option
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                if (radioButton.getId() == selectedId) {
                    Question question = currentQuiz.getQuestions().get(i);
                    userAnswers.put(question.getId(), j);
                    
                    // Check if answer is correct
                    boolean isCorrect = (j == question.getCorrectAnswerIndex());
                    correctAnswers[i] = isCorrect;
                    
                    // Show result icon
                    ImageView statusIcon = questionStatusIcons.get(i);
                    statusIcon.setVisibility(View.VISIBLE);
                    if (isCorrect) {
                        statusIcon.setImageResource(android.R.drawable.presence_online);
                        statusIcon.setColorFilter(Color.GREEN);
                        // Disable the radio group
                        for (int k = 0; k < radioGroup.getChildCount(); k++) {
                            radioGroup.getChildAt(k).setEnabled(!isCorrect);
                        }
                    } else {
                        statusIcon.setImageResource(android.R.drawable.presence_busy);
                        statusIcon.setColorFilter(Color.RED);
                    }
                    
                    break;
                }
            }
        }
        
        if (allAnswered) {
            quizSubmitted = true;
            
            // Update button text based on results
            if (allAnswersCorrect()) {
                submitButton.setText("Complete Quiz");
                
                // Mark quiz as completed
                User user = sessionManager.getCurrentUser();
                if (user != null) {
                    if (user.getCompletedQuizIds() == null) {
                        user.setCompletedQuizIds(new ArrayList<>());
                    }
                    if (!user.getCompletedQuizIds().contains(quizId)) {
                        user.getCompletedQuizIds().add(quizId);
                        sessionManager.saveUser(user);
                    }
                }
                
                Toast.makeText(this, "All answers correct! Skill achieved!", Toast.LENGTH_LONG).show();
            } else {
                submitButton.setText("Show Explanations");
            }
        }
    }
    
    private boolean allAnswersCorrect() {
        for (boolean isCorrect : correctAnswers) {
            if (!isCorrect) {
                return false;
            }
        }
        return true;
    }
} 