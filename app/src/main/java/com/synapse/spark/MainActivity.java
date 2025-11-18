package com.synapse.spark;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.synapse.spark.activity.QuizOptionsActivity;
import com.synapse.spark.activity.ScoreActivity;
import com.synapse.spark.databinding.ActivityMainBinding;

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