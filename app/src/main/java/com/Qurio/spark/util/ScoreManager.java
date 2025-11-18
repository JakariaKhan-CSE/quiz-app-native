package com.Qurio.spark.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreManager {

    private static final String PREF_NAME = "BrainStormPrefs";
    private static final String KEY_HIGH_SCORE = "high_score";
    private SharedPreferences sharedPreferences;

    public ScoreManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int currentHighScore = getHighScore();
        if (score > currentHighScore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.apply();
        }
    }

    public int getHighScore() {
        return sharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }
}