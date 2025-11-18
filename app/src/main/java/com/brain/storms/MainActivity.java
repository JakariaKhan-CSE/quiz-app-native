package com.brain.storms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.brain.storms.activity.QuizOptionsActivity;
import com.brain.storms.activity.ScoreActivity;
import com.brain.storms.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // "Start" button
        binding.btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizOptionsActivity.class);
            startActivity(intent);
        });

        // "Record" (High Score) button
        binding.btnRecord.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(intent);
        });

        // "Quit" button
        binding.btnQuit.setOnClickListener(v -> {
            finishAffinity(); // Exits the app completely
        });
    }
}