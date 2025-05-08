package com.example.task61.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.task61.model.Question;
import com.example.task61.model.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LLMApiService {
    
    private static final String API_URL = "https://api.example.com/v1/generate"; // Replace with your actual API URL
    private final RequestQueue requestQueue;

    public interface QuizGenerationCallback {
        void onSuccess(Quiz quiz);
        void onError(String errorMessage);
    }

    public LLMApiService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void generateQuiz(String topic, final QuizGenerationCallback callback) {
        String quizId = UUID.randomUUID().toString();
        
        // For the demo app, we'll use the dummy quiz generator directly
        // This ensures consistent behavior without requiring an actual API
        Quiz dummyQuiz = generateDummyQuiz(topic, quizId);
        callback.onSuccess(dummyQuiz);
        
        /* This is the API implementation that would be used in a production app
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("topic", topic);
            requestBody.put("type", "quiz");
            requestBody.put("num_questions", 3);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Quiz quiz = parseQuizResponse(response, quizId, topic);
                                callback.onSuccess(quiz);
                            } catch (JSONException e) {
                                callback.onError("Failed to parse response: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError("Network error: " + error.getMessage());
                            // In a real app, we should generate dummy data when the API fails
                            Quiz dummyQuiz = generateDummyQuiz(topic, quizId);
                            callback.onSuccess(dummyQuiz);
                        }
                    }
            );

            requestQueue.add(request);
        } catch (JSONException e) {
            callback.onError("Failed to create request: " + e.getMessage());
            // Generate dummy data on error
            Quiz dummyQuiz = generateDummyQuiz(topic, quizId);
            callback.onSuccess(dummyQuiz);
        }
        */
    }

    private Quiz parseQuizResponse(JSONObject response, String quizId, String topic) throws JSONException {
        // This would need to be adapted to your specific API response format
        String title = response.getString("title");
        String description = response.getString("description");
        
        Quiz quiz = new Quiz(quizId, title, description, topic);
        
        JSONArray questionsArray = response.getJSONArray("questions");
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObj = questionsArray.getJSONObject(i);
            String questionId = UUID.randomUUID().toString();
            String questionText = questionObj.getString("question");
            JSONArray optionsArray = questionObj.getJSONArray("options");
            int correctAnswerIndex = questionObj.getInt("correct_answer_index");
            String explanation = questionObj.getString("explanation");

            List<String> options = new ArrayList<>();
            for (int j = 0; j < optionsArray.length(); j++) {
                options.add(optionsArray.getString(j));
            }

            Question question = new Question(questionId, questionText, options, correctAnswerIndex, explanation);
            quiz.addQuestion(question);
        }
        
        return quiz;
    }

    // Generate dummy quiz for when API fails or for testing
    private Quiz generateDummyQuiz(String topic, String quizId) {
        String title = "Quiz on " + topic;
        String description = "Test your knowledge on " + topic;
        
        Quiz quiz = new Quiz(quizId, title, description, topic);

        // Create appropriate questions based on topic
        if (topic.contains("Algorithm") || topic.contains("Sorting")) {
            // Algorithm questions
            List<String> options1 = new ArrayList<>();
            options1.add("O(n log n)");
            options1.add("O(n²)");
            options1.add("O(n)");
            Question q1 = new Question(
                    "q1",
                    "What is the time complexity of QuickSort in the average case?",
                    options1,
                    0,
                    "QuickSort has an average time complexity of O(n log n), making it efficient for large datasets."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("Dijkstra's algorithm");
            options2.add("Binary search");
            options2.add("Bubble sort");
            Question q2 = new Question(
                    "q2",
                    "Which algorithm is used for finding the shortest path in a weighted graph?",
                    options2,
                    0,
                    "Dijkstra's algorithm is used to find the shortest paths between nodes in a graph."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("O(log n)");
            options3.add("O(n)");
            options3.add("O(1)");
            Question q3 = new Question(
                    "q3",
                    "What is the time complexity of binary search?",
                    options3,
                    0,
                    "Binary search has a time complexity of O(log n) because it halves the search range in each step."
            );
            quiz.addQuestion(q3);
        } 
        else if (topic.contains("Data Structure") || topic.contains("Array") || topic.contains("Tree")) {
            // Data Structure questions
            List<String> options1 = new ArrayList<>();
            options1.add("Linear");
            options1.add("Non-linear");
            options1.add("Both");
            Question q1 = new Question(
                    "q1",
                    "Which type of data structure is a tree?",
                    options1,
                    1,
                    "Trees are non-linear data structures that represent hierarchical relationships."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("O(1)");
            options2.add("O(log n)");
            options2.add("O(n)");
            Question q2 = new Question(
                    "q2",
                    "What is the time complexity of searching an element in a binary search tree (average case)?",
                    options2,
                    1,
                    "Binary search trees have an average search time complexity of O(log n)."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("Stack");
            options3.add("Queue");
            options3.add("Linked List");
            Question q3 = new Question(
                    "q3",
                    "Which data structure operates on a LIFO (Last In First Out) principle?",
                    options3,
                    0,
                    "Stack follows Last-In-First-Out (LIFO) principle where the element added last is accessed first."
            );
            quiz.addQuestion(q3);
        }
        else if (topic.contains("Web") || topic.contains("Front") || topic.contains("Back")) {
            // Web Development questions
            List<String> options1 = new ArrayList<>();
            options1.add("HTML");
            options1.add("CSS");
            options1.add("JavaScript");
            Question q1 = new Question(
                    "q1",
                    "Which language is primarily used for adding interactivity to web pages?",
                    options1,
                    2,
                    "JavaScript is used to make web pages interactive and dynamic."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("Node.js");
            options2.add("React");
            options2.add("Angular");
            Question q2 = new Question(
                    "q2",
                    "Which of these is a server-side JavaScript runtime environment?",
                    options2,
                    0,
                    "Node.js is a JavaScript runtime built on Chrome's V8 JavaScript engine for server-side programming."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("HTML");
            options3.add("CSS");
            options3.add("PHP");
            Question q3 = new Question(
                    "q3",
                    "Which language is primarily used for styling web pages?",
                    options3,
                    1,
                    "CSS (Cascading Style Sheets) is used to control the layout and appearance of web pages."
            );
            quiz.addQuestion(q3);
        }
        else if (topic.contains("Test")) {
            // Testing questions
            List<String> options1 = new ArrayList<>();
            options1.add("White box testing");
            options1.add("Black box testing");
            options1.add("Gray box testing");
            Question q1 = new Question(
                    "q1",
                    "Which testing type focuses on the internal structure of the code?",
                    options1,
                    0,
                    "White box testing examines the internal structure and workings of an application."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("JUnit");
            options2.add("Selenium");
            options2.add("Postman");
            Question q2 = new Question(
                    "q2",
                    "Which of these is commonly used for unit testing in Java?",
                    options2,
                    0,
                    "JUnit is a popular unit testing framework for Java applications."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("Test effectiveness");
            options3.add("Test coverage");
            options3.add("Test prioritization");
            Question q3 = new Question(
                    "q3",
                    "What metric measures how much of the code is executed during testing?",
                    options3,
                    1,
                    "Test coverage measures the percentage of code that is executed when running tests, helping identify untested parts of the program."
            );
            quiz.addQuestion(q3);
        }
        else if (topic.contains("Search")) {
            // Specific search algorithm questions
            List<String> options1 = new ArrayList<>();
            options1.add("O(log n)");
            options1.add("O(n)");
            options1.add("O(n²)");
            Question q1 = new Question(
                    "q1",
                    "What is the time complexity of binary search?",
                    options1,
                    0,
                    "Binary search has a time complexity of O(log n) because it divides the search interval in half with each comparison."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("Dijkstra's algorithm");
            options2.add("Binary search");
            options2.add("Bubble sort");
            Question q2 = new Question(
                    "q2",
                    "Which algorithm is used for finding the shortest path in a weighted graph?",
                    options2,
                    0,
                    "Dijkstra's algorithm is used to find the shortest paths between nodes in a graph."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("Linear search");
            options3.add("Binary search");
            options3.add("Depth-first search");
            Question q3 = new Question(
                    "q3",
                    "Which search algorithm requires the data to be sorted first?",
                    options3,
                    1,
                    "Binary search requires the data to be sorted before it can be used, as it relies on comparing the middle element and eliminating half the remaining elements."
            );
            quiz.addQuestion(q3);
        }
        else {
            // General knowledge questions as fallback
            List<String> options1 = new ArrayList<>();
            options1.add("Answer 1A");
            options1.add("Answer 1B");
            options1.add("Answer 1C");
            Question q1 = new Question(
                    "q1",
                    "Sample question 1 about " + topic + "?",
                    options1,
                    0,
                    "Explanation for question 1."
            );
            quiz.addQuestion(q1);
            
            List<String> options2 = new ArrayList<>();
            options2.add("Answer 2A");
            options2.add("Answer 2B");
            options2.add("Answer 2C");
            Question q2 = new Question(
                    "q2",
                    "Sample question 2 about " + topic + "?",
                    options2,
                    1,
                    "Explanation for question 2."
            );
            quiz.addQuestion(q2);
            
            List<String> options3 = new ArrayList<>();
            options3.add("Answer 3A");
            options3.add("Answer 3B");
            options3.add("Answer 3C");
            Question q3 = new Question(
                    "q3",
                    "Sample question 3 about " + topic + "?",
                    options3,
                    2,
                    "Explanation for question 3."
            );
            quiz.addQuestion(q3);
        }
        
        return quiz;
    }
} 