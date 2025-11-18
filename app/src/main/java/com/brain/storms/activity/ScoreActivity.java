package com.brain.storms.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.brain.storms.MainActivity;
import com.brain.storms.databinding.ActivityScoreBinding;
import com.brain.storms.util.ScoreManager;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Retrieve and Display High Score
        ScoreManager scoreManager = new ScoreManager(this);
        int highScore = scoreManager.getHighScore();

        // Retrieve current score if passed from QuizActivity (Optional, but good for UX)
        int currentScore = getIntent().getIntExtra("CURRENT_SCORE", 0);

        // Logic: If the current score is the new high score, you might want to show a special message
        if (currentScore >= highScore && currentScore > 0) {
            binding.tvTitle.setText("New Record!");
            binding.tvSubTitle.setText("You beat your personal best.");
        }

        // Bind data to UI
        binding.tvHighScore.setText(String.valueOf(highScore));

        // 2. Setup "Play Again" Button Logic
        binding.btnPlayAgain.setOnClickListener(v -> {
            // Go back to Game Setup (QuizOptionsActivity)
            Intent intent = new Intent(ScoreActivity.this, QuizOptionsActivity.class);
            // Clear the back stack so the user cannot press "Back" to return to the Score screen
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close current activity
        });

        // 3. Setup "Back to Menu" Button Logic
        binding.btnHome.setOnClickListener(v -> {
            // Go back to Main Menu (MainActivity)
            Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}