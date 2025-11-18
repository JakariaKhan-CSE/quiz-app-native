package com.Qurio.spark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.Qurio.spark.databinding.ActivityQuizOptionsBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuizOptionsActivity extends AppCompatActivity {

    private ActivityQuizOptionsBinding binding;
    private HashMap<String, Integer> categoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Setup the logic
        setupDropdowns();

        // 2. Setup Back Button (Added this since it is in your XML)
        binding.btnBack.setOnClickListener(v -> finish());

        // 3. Start Button Logic
        binding.btnStartQuiz.setOnClickListener(v -> {
            // FIX: Use getText() instead of getSelectedItem()
            String difficulty = binding.spinnerDifficulty.getText().toString();
            String categoryName = binding.spinnerCategory.getText().toString();

            // Get ID from map, default to 0 (Any) if not found
            int categoryId = categoryMap.getOrDefault(categoryName, 0);

            // Start QuizActivity
            Intent intent = new Intent(QuizOptionsActivity.this, QuizActivity.class);
            intent.putExtra("DIFFICULTY", difficulty);
            intent.putExtra("CATEGORY_ID", categoryId);
            intent.putExtra("AMOUNT", 15);
            startActivity(intent);
        });
    }

    private void setupDropdowns() {
        // --- A. PREPARE DATA ---
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

        // Extract keys and sort them so the list looks neat
        List<String> categories = new ArrayList<>(categoryMap.keySet());
        Collections.sort(categories);
        // Ensure "Any Category" is always at the top
        categories.remove("Any Category");
        categories.add(0, "Any Category");

        String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};

        // --- B. SETUP ADAPTERS ---
        // Use 'simple_dropdown_item_1line' which looks better in Material Dropdowns
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);

        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, difficulties);

        // --- C. ATTACH TO VIEWS ---
        binding.spinnerCategory.setAdapter(categoryAdapter);
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);

        // --- D. SET DEFAULT VALUES ---
        // We must use setText(val, false) so it doesn't trigger the filter immediately
        binding.spinnerCategory.setText(categories.get(0), false);
        binding.spinnerDifficulty.setText(difficulties[0], false);
    }
}