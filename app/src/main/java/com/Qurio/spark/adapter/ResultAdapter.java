package com.Qurio.spark.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Qurio.spark.R;
import com.Qurio.spark.models.Question;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<Question> questionList;
    private Context context;

    public ResultAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_answer, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvQuestion.setText(Html.fromHtml(question.getQuestion()));

        String userAnswer = question.getUserAnswer();
        String correctAnswer = Html.fromHtml(question.getCorrectAnswer()).toString();

        holder.tvCorrectAnswer.setText("Correct Answer: " + correctAnswer);

        if (userAnswer == null) {
            holder.tvUserAnswer.setText("Your Answer: (No Answer)");
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.neutral_bg));
        } else if (userAnswer.equals(correctAnswer)) {
            holder.tvUserAnswer.setText("Your Answer: " + userAnswer);
            holder.tvUserAnswer.setTextColor(ContextCompat.getColor(context, R.color.correct_answer));
        } else {
            holder.tvUserAnswer.setText("Your Answer: " + userAnswer);
            holder.tvUserAnswer.setTextColor(ContextCompat.getColor(context, R.color.wrong_answer));
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvUserAnswer, tvCorrectAnswer;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvResultQuestion);
            tvUserAnswer = itemView.findViewById(R.id.tvUserAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        }
    }
}