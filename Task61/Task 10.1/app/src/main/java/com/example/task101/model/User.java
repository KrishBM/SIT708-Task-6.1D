package com.example.task101.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String profileImageUrl;
    private List<String> interests;
    private List<String> completedQuizIds;
    private Date registrationDate;
    private SubscriptionTier subscriptionTier;
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private String shareableProfileId;
    private boolean isProfilePublic;

    public enum SubscriptionTier {
        STARTER("Starter", 0),
        INTERMEDIATE("Intermediate", 4.99),
        ADVANCED("Advanced", 9.99);

        private final String displayName;
        private final double price;

        SubscriptionTier(String displayName, double price) {
            this.displayName = displayName;
            this.price = price;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getPrice() {
            return price;
        }
    }

    public User() {
        // Required empty constructor for Firebase
        this.interests = new ArrayList<>();
        this.completedQuizIds = new ArrayList<>();
        this.registrationDate = new Date();
        this.subscriptionTier = SubscriptionTier.STARTER;
        this.totalQuestions = 0;
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
        this.isProfilePublic = false;
    }

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.interests = new ArrayList<>();
        this.completedQuizIds = new ArrayList<>();
        this.registrationDate = new Date();
        this.subscriptionTier = SubscriptionTier.STARTER;
        this.totalQuestions = 0;
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
        this.isProfilePublic = false;
    }

    // Existing getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void addInterest(String interest) {
        if (this.interests == null) {
            this.interests = new ArrayList<>();
        }
        if (!this.interests.contains(interest)) {
            this.interests.add(interest);
        }
    }

    public void removeInterest(String interest) {
        if (this.interests != null) {
            this.interests.remove(interest);
        }
    }
    
    public List<String> getCompletedQuizIds() {
        return completedQuizIds;
    }

    public void setCompletedQuizIds(List<String> completedQuizIds) {
        this.completedQuizIds = completedQuizIds;
    }
    
    public boolean hasCompletedQuiz(String quizId) {
        return completedQuizIds != null && completedQuizIds.contains(quizId);
    }

    // New getters and setters for enhanced features
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public SubscriptionTier getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(SubscriptionTier subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getShareableProfileId() {
        return shareableProfileId;
    }

    public void setShareableProfileId(String shareableProfileId) {
        this.shareableProfileId = shareableProfileId;
    }

    public boolean isProfilePublic() {
        return isProfilePublic;
    }

    public void setProfilePublic(boolean profilePublic) {
        isProfilePublic = profilePublic;
    }

    // Helper methods
    public void incrementTotalQuestions() {
        this.totalQuestions++;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
        incrementTotalQuestions();
    }

    public void incrementIncorrectAnswers() {
        this.incorrectAnswers++;
        incrementTotalQuestions();
    }

    public double getAccuracyPercentage() {
        if (totalQuestions == 0) return 0.0;
        return (double) correctAnswers / totalQuestions * 100;
    }
} 