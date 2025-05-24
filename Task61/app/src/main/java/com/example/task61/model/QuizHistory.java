package com.example.task61.model;

import java.util.Date;
import java.util.List;

public class QuizHistory {
    private String id;
    private String userId;
    private String quizId;
    private String quizTitle;
    private String category;
    private List<QuestionResult> questionResults;
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private double accuracyPercentage;
    private Date completedAt;
    private long timeTaken; // in milliseconds

    public static class QuestionResult {
        private String questionId;
        private String questionText;
        private int selectedAnswerIndex;
        private int correctAnswerIndex;
        private boolean isCorrect;
        private String explanation;

        public QuestionResult() {}

        public QuestionResult(String questionId, String questionText, int selectedAnswerIndex, 
                            int correctAnswerIndex, boolean isCorrect, String explanation) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.selectedAnswerIndex = selectedAnswerIndex;
            this.correctAnswerIndex = correctAnswerIndex;
            this.isCorrect = isCorrect;
            this.explanation = explanation;
        }

        // Getters and setters for QuestionResult
        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }

        public int getSelectedAnswerIndex() { return selectedAnswerIndex; }
        public void setSelectedAnswerIndex(int selectedAnswerIndex) { this.selectedAnswerIndex = selectedAnswerIndex; }

        public int getCorrectAnswerIndex() { return correctAnswerIndex; }
        public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    public QuizHistory() {
        this.completedAt = new Date();
    }

    public QuizHistory(String userId, String quizId, String quizTitle, String category) {
        this.userId = userId;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.category = category;
        this.completedAt = new Date();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<QuestionResult> getQuestionResults() { return questionResults; }
    public void setQuestionResults(List<QuestionResult> questionResults) { 
        this.questionResults = questionResults;
        calculateResults();
    }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(int incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }

    public double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }

    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }

    public long getTimeTaken() { return timeTaken; }
    public void setTimeTaken(long timeTaken) { this.timeTaken = timeTaken; }

    // Helper method to calculate results
    private void calculateResults() {
        if (questionResults == null) return;
        
        this.totalQuestions = questionResults.size();
        this.correctAnswers = 0;
        
        for (QuestionResult result : questionResults) {
            if (result.isCorrect()) {
                this.correctAnswers++;
            }
        }
        
        this.incorrectAnswers = this.totalQuestions - this.correctAnswers;
        this.accuracyPercentage = this.totalQuestions > 0 ? 
            (double) this.correctAnswers / this.totalQuestions * 100 : 0.0;
    }

    public String getFormattedTimeTaken() {
        long seconds = timeTaken / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
} 