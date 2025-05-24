package com.example.task61.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.task61.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "PersonalizedLearningApp";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_DATA = "userData";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        
        // Save complete user object as JSON
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);
        
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }
        
        String userJson = pref.getString(KEY_USER_DATA, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        
        // Fallback to basic user data if JSON is not available
        String userId = pref.getString(KEY_USER_ID, null);
        String username = pref.getString(KEY_USERNAME, null);
        String email = pref.getString(KEY_EMAIL, null);
        
        if (userId != null && username != null && email != null) {
            return new User(userId, username, email);
        }
        
        return null;
    }

    public void saveUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.commit();
    }

    public void updateUserStats(int totalQuestions, int correctAnswers, int incorrectAnswers) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            currentUser.setTotalQuestions(totalQuestions);
            currentUser.setCorrectAnswers(correctAnswers);
            currentUser.setIncorrectAnswers(incorrectAnswers);
            saveUser(currentUser);
        }
    }

    public void incrementUserStats(boolean isCorrect) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            if (isCorrect) {
                currentUser.incrementCorrectAnswers();
            } else {
                currentUser.incrementIncorrectAnswers();
            }
            saveUser(currentUser);
        }
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, null);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void clearSession() {
        logout();
    }
} 