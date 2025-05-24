package com.example.task61.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61.QuizActivity;
import com.example.task61.R;
import com.example.task61.model.Quiz;
import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    
    private Context context;
    private List<Quiz> quizzes;
    private SessionManager sessionManager;

    public TaskAdapter(Context context, List<Quiz> quizzes) {
        this.context = context;
        this.quizzes = quizzes;
        this.sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Quiz quiz = quizzes.get(position);
        holder.titleTextView.setText(quiz.getTitle());
        holder.descriptionTextView.setText(quiz.getDescription());
        
        // Check if user has completed this quiz
        User user = sessionManager.getCurrentUser();
        final boolean isCompleted;
        
        if (user != null && user.getCompletedQuizIds() != null) {
            isCompleted = user.getCompletedQuizIds().contains(quiz.getId());
        } else {
            isCompleted = false;
        }
        
        // Set the status indicator based on completion status
        if (isCompleted) {
            // Change from square to checkmark
            holder.statusIndicator.setBackgroundResource(0); // Remove background
            holder.statusIndicator.setImageResource(R.drawable.ic_checkmark); // Use our custom checkmark icon
            holder.statusIndicator.setVisibility(View.VISIBLE); // Ensure it's visible
            holder.statusTextView.setText("Completed");
            holder.statusTextView.setTextColor(Color.parseColor("#4CAF50")); // Green text
            holder.statusTextView.setVisibility(View.VISIBLE);
            
            // Apply visual effects to indicate the quiz is disabled
            holder.itemView.setAlpha(0.7f);
            holder.taskArrowIcon.setVisibility(View.GONE); // Hide the play arrow for completed quizzes
        } else {
            // Keep original orange square
            holder.statusIndicator.setImageDrawable(null); // Clear any image
            holder.statusIndicator.setBackgroundResource(android.R.color.holo_orange_light);
            holder.statusIndicator.setVisibility(View.VISIBLE); // Ensure it's visible
            holder.statusTextView.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f); // Full opacity for active quizzes
            holder.taskArrowIcon.setVisibility(View.VISIBLE); // Show the play arrow for active quizzes
        }
        
        // Set click listener to navigate to quiz activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompleted) {
                    Toast.makeText(context, "You've already completed this quiz!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("quiz_id", quiz.getId());
                intent.putExtra("quiz_title", quiz.getTitle());
                intent.putExtra("quiz_description", quiz.getDescription());
                intent.putExtra("quiz_category", quiz.getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public void updateQuizzes(List<Quiz> newQuizzes) {
        this.quizzes = newQuizzes;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView statusIndicator;
        TextView statusTextView;
        ImageView taskArrowIcon;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitleText);
            descriptionTextView = itemView.findViewById(R.id.taskDescriptionText);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            taskArrowIcon = itemView.findViewById(R.id.taskArrowIcon);
        }
    }
} 