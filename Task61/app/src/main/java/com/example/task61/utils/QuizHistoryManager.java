package com.example.task61.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.task61.model.QuizHistory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuizHistoryManager {
    private static final String PREF_NAME = "quiz_history_pref";
    private static final String KEY_QUIZ_HISTORY = "quiz_history_list";
    
    private SharedPreferences preferences;
    private Gson gson;
    
    public QuizHistoryManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public void saveQuizHistory(QuizHistory quizHistory) {
        List<QuizHistory> historyList = getAllQuizHistory();
        historyList.add(0, quizHistory); // Add to beginning of list (most recent first)
        
        String json = gson.toJson(historyList);
        preferences.edit().putString(KEY_QUIZ_HISTORY, json).apply();
    }
    
    public List<QuizHistory> getAllQuizHistory() {
        String json = preferences.getString(KEY_QUIZ_HISTORY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type listType = new TypeToken<List<QuizHistory>>(){}.getType();
        List<QuizHistory> historyList = gson.fromJson(json, listType);
        return historyList != null ? historyList : new ArrayList<>();
    }
    
    public List<QuizHistory> getQuizHistoryForUser(String userId) {
        List<QuizHistory> allHistory = getAllQuizHistory();
        List<QuizHistory> userHistory = new ArrayList<>();
        
        for (QuizHistory history : allHistory) {
            if (history.getUserId() != null && history.getUserId().equals(userId)) {
                userHistory.add(history);
            }
        }
        
        return userHistory;
    }
    
    public void clearAllHistory() {
        preferences.edit().remove(KEY_QUIZ_HISTORY).apply();
    }
    
    public int getQuizCount(String userId) {
        return getQuizHistoryForUser(userId).size();
    }
} 