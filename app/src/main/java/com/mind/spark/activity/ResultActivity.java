package com.mind.spark.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mind.spark.MainActivity;
import com.mind.spark.adapter.ResultAdapter;
import com.mind.spark.databinding.ActivityResultBinding;
import com.mind.spark.models.Question;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get data from QuizActivity
        int score = getIntent().getIntExtra("SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        ArrayList<Question> questionList = getIntent().getParcelableArrayListExtra("QUESTION_LIST");

        // Display score
        binding.tvScore.setText("Your Score: " + score + " / " + totalQuestions);

        // Setup RecyclerView
        binding.rvResults.setLayoutManager(new LinearLayoutManager(this));
        ResultAdapter adapter = new ResultAdapter(questionList);
        binding.rvResults.setAdapter(adapter);

        // "Finish" button to go home
        binding.btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}