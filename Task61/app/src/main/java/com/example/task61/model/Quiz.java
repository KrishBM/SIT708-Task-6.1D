package com.example.task61.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
    private String category;
    private boolean isCompleted;

    public Quiz() {
        // Required empty constructor
        this.questions = new ArrayList<>();
    }

    public Quiz(String id, String title, String description, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.questions = new ArrayList<>();
        this.isCompleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
} 