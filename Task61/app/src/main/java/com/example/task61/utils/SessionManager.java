package com.example.task61.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.task61.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "LearningAppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PROFILE_IMAGE = "userProfileImage";
    private static final String KEY_USER_DETAILS = "userDetails";
    
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;
    private final Gson gson;
    
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }
    
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }
    
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void saveUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PROFILE_IMAGE, user.getProfileImageUrl());
        
        // Save the complete user object as JSON
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DETAILS, userJson);
        
        editor.commit();
    }
    
    public User getUser() {
        String userJson = pref.getString(KEY_USER_DETAILS, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        
        // Fallback to retrieve from individual fields if full object isn't available
        String id = pref.getString(KEY_USER_ID, null);
        String name = pref.getString(KEY_USER_NAME, null);
        String email = pref.getString(KEY_USER_EMAIL, null);
        String profileImage = pref.getString(KEY_USER_PROFILE_IMAGE, null);
        
        if (id != null && name != null) {
            User user = new User(id, name, email);
            user.setProfileImageUrl(profileImage);
            return user;
        }
        
        return null;
    }
    
    public void logout() {
        editor.clear();
        editor.commit();
    }
} 