package com.example.task61.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String profileImageUrl;
    private List<String> interests;
    private List<String> completedQuizIds;

    public User() {
        // Required empty constructor for Firebase
        this.interests = new ArrayList<>();
        this.completedQuizIds = new ArrayList<>();
    }

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.interests = new ArrayList<>();
        this.completedQuizIds = new ArrayList<>();
    }

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
} 