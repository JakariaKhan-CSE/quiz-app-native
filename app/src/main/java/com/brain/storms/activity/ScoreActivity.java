package com.brain.storms.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.brain.storms.databinding.ActivityScoreBinding;
import com.brain.storms.util.ScoreManager;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ScoreManager scoreManager = new ScoreManager(this);
        int highScore = scoreManager.getHighScore();

        binding.tvHighScore.setText(String.valueOf(highScore));
    }
}