package com.example.task61.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61.R;
import com.example.task61.model.QuizHistory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder> {

    private Context context;
    private List<QuizHistory> quizHistoryList;
    private SimpleDateFormat dateFormat;

    public QuizHistoryAdapter(Context context, List<QuizHistory> quizHistoryList) {
        this.context = context;
        this.quizHistoryList = quizHistoryList;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public QuizHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_history, parent, false);
        return new QuizHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHistoryViewHolder holder, int position) {
        QuizHistory history = quizHistoryList.get(position);
        
        holder.quizTitleText.setText(history.getQuizTitle());
        holder.categoryText.setText(history.getCategory());
        holder.dateText.setText(dateFormat.format(history.getCompletedAt()));
        
        // Score display
        String score = history.getCorrectAnswers() + "/" + history.getTotalQuestions();
        holder.scoreText.setText(score);
        
        // Accuracy display
        String accuracy = String.format(Locale.getDefault(), "%.1f%%", history.getAccuracyPercentage());
        holder.accuracyText.setText(accuracy);
        
        // Time taken display
        holder.timeText.setText(history.getFormattedTimeTaken());
        
        // Set accuracy indicator color based on performance
        int indicatorColor;
        if (history.getAccuracyPercentage() >= 80) {
            indicatorColor = ContextCompat.getColor(context, R.color.success);
        } else if (history.getAccuracyPercentage() >= 60) {
            indicatorColor = ContextCompat.getColor(context, R.color.warning);
        } else {
            indicatorColor = ContextCompat.getColor(context, R.color.error);
        }
        holder.accuracyIndicator.setBackgroundColor(indicatorColor);
    }

    @Override
    public int getItemCount() {
        return quizHistoryList.size();
    }

    public static class QuizHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitleText;
        TextView categoryText;
        TextView dateText;
        TextView scoreText;
        TextView accuracyText;
        TextView timeText;
        View accuracyIndicator;

        public QuizHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitleText = itemView.findViewById(R.id.quiz_title_text);
            categoryText = itemView.findViewById(R.id.category_text);
            dateText = itemView.findViewById(R.id.date_text);
            scoreText = itemView.findViewById(R.id.score_text);
            accuracyText = itemView.findViewById(R.id.accuracy_text);
            timeText = itemView.findViewById(R.id.time_text);
            accuracyIndicator = itemView.findViewById(R.id.accuracy_indicator);
        }
    }
} 