package com.brain.storms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.brain.storms.databinding.ActivityQuizOptionsBinding;
import java.util.HashMap;

public class QuizOptionsActivity extends AppCompatActivity {

    private ActivityQuizOptionsBinding binding;
    // Map to store category names and their API IDs
    private HashMap<String, Integer> categoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinners();

        binding.btnStartQuiz.setOnClickListener(v -> {
            // Get selected difficulty
            String difficulty = binding.spinnerDifficulty.getSelectedItem().toString();

            // Get selected category ID
            String categoryName = binding.spinnerCategory.getSelectedItem().toString();
            int categoryId = categoryMap.get(categoryName);

            // Start QuizActivity
            Intent intent = new Intent(QuizOptionsActivity.this, QuizActivity.class);
            intent.putExtra("DIFFICULTY", difficulty);
            intent.putExtra("CATEGORY_ID", categoryId);
            intent.putExtra("AMOUNT", 15); // Default 15 questions
            startActivity(intent);
        });
    }

    private void setupSpinners() {
        // Populate Category Map
        // You can get the full list from: https://opentdb.com/api_category.php
        categoryMap.put("Any Category", 0);
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Books", 10);
        categoryMap.put("Film", 11);
        categoryMap.put("Music", 12);
        categoryMap.put("Science & Nature", 17);
        categoryMap.put("Computers", 18);
        categoryMap.put("Sports", 21);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);

        // Setup Category Spinner
        String[] categories = categoryMap.keySet().toArray(new String[0]);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // Setup Difficulty Spinner
        String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);
    }
}