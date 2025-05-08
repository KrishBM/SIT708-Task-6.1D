package com.example.task61.model;

import java.util.List;

public class Question {
    private String id;
    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;
    private String explanation;

    public Question() {
        // Required empty constructor
    }

    public Question(String id, String questionText, List<String> options, int correctAnswerIndex, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isCorrectAnswer(int selectedAnswerIndex) {
        return selectedAnswerIndex == correctAnswerIndex;
    }
} 