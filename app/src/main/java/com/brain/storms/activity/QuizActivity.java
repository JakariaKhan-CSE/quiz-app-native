package com.brain.storms.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.brain.storms.R;
import com.brain.storms.databinding.ActivityQuizBinding;
import com.brain.storms.models.Question;
import com.brain.storms.util.ScoreManager;
import com.brain.storms.viewmodel.QuizViewModel;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private QuizViewModel quizViewModel;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private ArrayList<Question> finishedQuestions = new ArrayList<>();
    private Button selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get options from Intent
        Intent intent = getIntent();
        int amount = intent.getIntExtra("AMOUNT", 15);
        int categoryId = intent.getIntExtra("CATEGORY_ID", 0);
        String difficulty = intent.getStringExtra("DIFFICULTY");
        if (difficulty.equals("Any Difficulty")) {
            difficulty = ""; // API expects empty string for any
        }

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        setupObservers();
        quizViewModel.fetchQuestions(amount, categoryId, difficulty);

        binding.btnOption1.setOnClickListener(v -> onOptionClick((Button) v));
        binding.btnOption2.setOnClickListener(v -> onOptionClick((Button) v));
        binding.btnOption3.setOnClickListener(v -> onOptionClick((Button) v));
        binding.btnOption4.setOnClickListener(v -> onOptionClick((Button) v));
    }

    private void setupObservers() {
        quizViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.quizContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        quizViewModel.getError().observe(this, error -> {
            if (error != null) {
                binding.tvQuestion.setText(error);
            }
        });

        quizViewModel.getQuestionList().observe(this, questions -> {
            if (questions != null && !questions.isEmpty()) {
                questionList = questions;
                loadNextQuestion();
            } else {
                binding.tvQuestion.setText("No questions found for these settings.");
            }
        });
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            resetButtonStyles();
            Question question = questionList.get(currentQuestionIndex);

            // Add to list for results screen
            finishedQuestions.add(question);

            binding.tvQuestionCount.setText("Question: " + (currentQuestionIndex + 1) + "/" + questionList.size());
            binding.tvQuestion.setText(Html.fromHtml(question.getQuestion()));

            List<String> answers = question.getAllAnswers();
            binding.btnOption1.setText(Html.fromHtml(answers.get(0)));
            binding.btnOption2.setText(Html.fromHtml(answers.get(1)));
            binding.btnOption3.setText(Html.fromHtml(answers.get(2)));
            binding.btnOption4.setText(Html.fromHtml(answers.get(3)));

            enableOptions(true);
        } else {
            // End of quiz
            finishQuiz();
        }
    }

    private void onOptionClick(Button button) {
        enableOptions(false); // Disable buttons after selection
        selectedButton = button;
        String userAnswer = button.getText().toString();
        Question currentQuestion = questionList.get(currentQuestionIndex);
        currentQuestion.setUserAnswer(userAnswer); // Save user's answer

        String correctAnswer = Html.fromHtml(currentQuestion.getCorrectAnswer()).toString();

        if (userAnswer.equals(correctAnswer)) {
            // Correct Answer
            score++;
            button.setBackgroundResource(R.drawable.btn_option_correct);
            button.setTextColor(Color.WHITE);
        } else {
            // Wrong Answer
            button.setBackgroundResource(R.drawable.btn_option_wrong);
            button.setTextColor(Color.WHITE);
            // Highlight the correct answer
            highlightCorrectAnswer(correctAnswer);
        }

        // Move to next question after a delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentQuestionIndex++;
            loadNextQuestion();
        }, 1500); // 1.5 second delay
    }

    private void highlightCorrectAnswer(String correctAnswer) {
        if (binding.btnOption1.getText().toString().equals(correctAnswer)) {
            binding.btnOption1.setBackgroundResource(R.drawable.btn_option_correct);
            binding.btnOption1.setTextColor(Color.WHITE);
        } else if (binding.btnOption2.getText().toString().equals(correctAnswer)) {
            binding.btnOption2.setBackgroundResource(R.drawable.btn_option_correct);
            binding.btnOption2.setTextColor(Color.WHITE);
        } else if (binding.btnOption3.getText().toString().equals(correctAnswer)) {
            binding.btnOption3.setBackgroundResource(R.drawable.btn_option_correct);
            binding.btnOption3.setTextColor(Color.WHITE);
        } else if (binding.btnOption4.getText().toString().equals(correctAnswer)) {
            binding.btnOption4.setBackgroundResource(R.drawable.btn_option_correct);
            binding.btnOption4.setTextColor(Color.WHITE);
        }
    }

    private void resetButtonStyles() {
        Button[] buttons = {binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4};
        for (Button btn : buttons) {
            btn.setBackgroundResource(R.drawable.btn_option_default);
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_dark));
        }
    }

    private void enableOptions(boolean enabled) {
        binding.btnOption1.setEnabled(enabled);
        binding.btnOption2.setEnabled(enabled);
        binding.btnOption3.setEnabled(enabled);
        binding.btnOption4.setEnabled(enabled);
    }

    private void finishQuiz() {
        // Save new high score
        ScoreManager scoreManager = new ScoreManager(this);
        scoreManager.saveHighScore(score);

        // Go to Result Activity
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questionList.size());
        intent.putParcelableArrayListExtra("QUESTION_LIST", finishedQuestions);
        startActivity(intent);
        finish(); // Finish this activity
    }
}