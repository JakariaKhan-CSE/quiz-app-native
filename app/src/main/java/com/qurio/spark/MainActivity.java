package com.qurio.spark;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.qurio.spark.activity.QuizOptionsActivity;
import com.qurio.spark.activity.ScoreActivity;
import com.qurio.spark.databinding.ActivityMainBinding;

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