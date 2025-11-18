package com.synapse.spark.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.synapse.spark.R;
import com.synapse.spark.databinding.ActivityQuizBinding;
import com.synapse.spark.models.Question;
import com.synapse.spark.util.ScoreManager;
import com.synapse.spark.viewmodel.QuizViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private QuizViewModel quizViewModel;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private ArrayList<Question> finishedQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Setup Back Button
        binding.btnBack.setOnClickListener(v -> finish());

        // 2. Get Intent Data
        Intent intent = getIntent();
        int amount = intent.getIntExtra("AMOUNT", 15);
        int categoryId = intent.getIntExtra("CATEGORY_ID", 0);
        String difficulty = intent.getStringExtra("DIFFICULTY");

        // API fix: "Any Difficulty" should be empty string
        if (difficulty == null || difficulty.equals("Any Difficulty")) {
            difficulty = "";
        }

        // 3. Initialize ViewModel
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        setupObservers();
        quizViewModel.fetchQuestions(amount, categoryId, difficulty);

        // 4. Setup Listeners
        binding.btnOption1.setOnClickListener(v -> onOptionClick(binding.btnOption1));
        binding.btnOption2.setOnClickListener(v -> onOptionClick(binding.btnOption2));
        binding.btnOption3.setOnClickListener(v -> onOptionClick(binding.btnOption3));
        binding.btnOption4.setOnClickListener(v -> onOptionClick(binding.btnOption4));
    }

    private void setupObservers() {
        quizViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.quizContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        quizViewModel.getError().observe(this, error -> {
            if (error != null) {
                binding.tvQuestion.setText("Error loading questions.\nPlease check your internet.");
            }
        });

        quizViewModel.getQuestionList().observe(this, questions -> {
            if (questions != null && !questions.isEmpty()) {
                questionList = questions;

                // Initialize Progress Bar Max
                binding.quizProgressBar.setMax(questionList.size());

                loadNextQuestion();
            } else {
                binding.tvQuestion.setText("No questions found for these settings.");
            }
        });
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            resetButtonStyles(); // Clear previous colors

            Question question = questionList.get(currentQuestionIndex);
            finishedQuestions.add(question);

            // Update UI Text
            binding.tvQuestionCount.setText((currentQuestionIndex + 1) + "/" + questionList.size());
            binding.tvQuestion.setText(Html.fromHtml(question.getQuestion(), Html.FROM_HTML_MODE_LEGACY));

            // Update Progress Bar (Animated)
            binding.quizProgressBar.setProgress(currentQuestionIndex + 1, true);

            // Set Options Text
            List<String> answers = question.getAllAnswers();
            binding.btnOption1.setText(Html.fromHtml(answers.get(0), Html.FROM_HTML_MODE_LEGACY));
            binding.btnOption2.setText(Html.fromHtml(answers.get(1), Html.FROM_HTML_MODE_LEGACY));
            binding.btnOption3.setText(Html.fromHtml(answers.get(2), Html.FROM_HTML_MODE_LEGACY));
            binding.btnOption4.setText(Html.fromHtml(answers.get(3), Html.FROM_HTML_MODE_LEGACY));

            enableOptions(true);
        } else {
            finishQuiz();
        }
    }

    private void onOptionClick(MaterialButton selectedButton) {
        enableOptions(false); // Prevent double clicking

        String userAnswer = selectedButton.getText().toString();
        Question currentQuestion = questionList.get(currentQuestionIndex);
        currentQuestion.setUserAnswer(userAnswer);

        String correctAnswer = Html.fromHtml(currentQuestion.getCorrectAnswer(), Html.FROM_HTML_MODE_LEGACY).toString();

        if (userAnswer.equals(correctAnswer)) {
            // --- CORRECT ---
            score++;
            setButtonState(selectedButton, R.color.correct_answer);
        } else {
            // --- WRONG ---
            setButtonState(selectedButton, R.color.wrong_answer);
            // Show the user the correct one
            highlightCorrectAnswer(correctAnswer);
        }

        // Delay before next question
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentQuestionIndex++;
            loadNextQuestion();
        }, 1500);
    }

    // Helper to find and highlight the correct button if user was wrong
    private void highlightCorrectAnswer(String correctAnswer) {
        MaterialButton[] buttons = {binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4};

        for (MaterialButton btn : buttons) {
            if (btn.getText().toString().equals(correctAnswer)) {
                setButtonState(btn, R.color.correct_answer);
                break;
            }
        }
    }

    // THE FIX: Programmatically styling MaterialButton
    private void setButtonState(MaterialButton btn, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);

        // 1. Fill the background color
        btn.setBackgroundColor(color);

        // 2. Match the border color
        btn.setStrokeColor(ColorStateList.valueOf(color));

        // 3. Change text to white for contrast
        btn.setTextColor(Color.WHITE);
    }

    // Reset to "Outlined" style
    private void resetButtonStyles() {
        MaterialButton[] buttons = {binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4};
        int defaultTextColor = ContextCompat.getColor(this, R.color.text_dark);
        int strokeColor = Color.parseColor("#D0D0D0");

        for (MaterialButton btn : buttons) {
            // Transparent background
            btn.setBackgroundColor(Color.TRANSPARENT);

            // Grey Border
            btn.setStrokeColor(ColorStateList.valueOf(strokeColor));
            btn.setStrokeWidth(2); // 2px width (approx 1dp)

            // Dark Text
            btn.setTextColor(defaultTextColor);
        }
    }

    private void enableOptions(boolean enabled) {
        binding.btnOption1.setEnabled(enabled);
        binding.btnOption2.setEnabled(enabled);
        binding.btnOption3.setEnabled(enabled);
        binding.btnOption4.setEnabled(enabled);
    }

    private void finishQuiz() {
        ScoreManager scoreManager = new ScoreManager(this);
        scoreManager.saveHighScore(score);

        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questionList.size());
        // Make sure your Question model implements Parcelable or Serializable
        intent.putParcelableArrayListExtra("QUESTION_LIST", finishedQuestions);

        startActivity(intent);
        finish();
    }
}