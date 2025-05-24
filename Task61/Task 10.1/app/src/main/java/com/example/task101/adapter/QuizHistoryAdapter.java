package com.example.task101.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task101.R;
import com.example.task101.model.QuizHistory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder> {

    private Context context;
    private List<QuizHistory> quizHistoryList;
    private OnQuizHistoryClickListener clickListener;

    public interface OnQuizHistoryClickListener {
        void onQuizHistoryClick(QuizHistory quizHistory);
    }

    public QuizHistoryAdapter(Context context, List<QuizHistory> quizHistoryList) {
        this.context = context;
        this.quizHistoryList = quizHistoryList;
    }

    public void setOnQuizHistoryClickListener(OnQuizHistoryClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public QuizHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_history, parent, false);
        return new QuizHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHistoryViewHolder holder, int position) {
        QuizHistory quizHistory = quizHistoryList.get(position);
        holder.bind(quizHistory);
    }

    @Override
    public int getItemCount() {
        return quizHistoryList.size();
    }

    class QuizHistoryViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView quizTitleText;
        private TextView categoryText;
        private TextView dateText;
        private TextView scoreText;
        private TextView accuracyText;
        private TextView timeText;
        private View accuracyIndicator;

        public QuizHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.quiz_history_card);
            quizTitleText = itemView.findViewById(R.id.quiz_title_text);
            categoryText = itemView.findViewById(R.id.category_text);
            dateText = itemView.findViewById(R.id.date_text);
            scoreText = itemView.findViewById(R.id.score_text);
            accuracyText = itemView.findViewById(R.id.accuracy_text);
            timeText = itemView.findViewById(R.id.time_text);
            accuracyIndicator = itemView.findViewById(R.id.accuracy_indicator);
        }

        public void bind(QuizHistory quizHistory) {
            quizTitleText.setText(quizHistory.getQuizTitle());
            categoryText.setText(quizHistory.getCategory());
            
            // Format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            dateText.setText(dateFormat.format(quizHistory.getCompletedAt()));
            
            // Score
            scoreText.setText(String.format(Locale.getDefault(), "%d/%d", 
                    quizHistory.getCorrectAnswers(), quizHistory.getTotalQuestions()));
            
            // Accuracy
            double accuracy = quizHistory.getAccuracyPercentage();
            accuracyText.setText(String.format(Locale.getDefault(), "%.1f%%", accuracy));
            
            // Time taken
            timeText.setText(quizHistory.getFormattedTimeTaken());
            
            // Set accuracy indicator color
            setAccuracyIndicatorColor(accuracy);
            
            // Click listener
            cardView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onQuizHistoryClick(quizHistory);
                }
            });
        }
        
        private void setAccuracyIndicatorColor(double accuracy) {
            int color;
            if (accuracy >= 80) {
                color = context.getResources().getColor(android.R.color.holo_green_light);
            } else if (accuracy >= 60) {
                color = context.getResources().getColor(android.R.color.holo_orange_light);
            } else {
                color = context.getResources().getColor(android.R.color.holo_red_light);
            }
            accuracyIndicator.setBackgroundColor(color);
        }
    }
} 
 