package com.qurio.spark.models;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Question implements Parcelable {

    @SerializedName("category")
    private String category;

    @SerializedName("type")
    private String type;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("question")
    private String question;

    @SerializedName("correct_answer")
    private String correctAnswer;

    @SerializedName("incorrect_answers")
    private List<String> incorrectAnswers;

    // --- Add these fields ---
    private List<String> allAnswers; // Will hold correct + incorrect shuffled
    private String userAnswer;       // To store what the user picked

    // --- Parcelable Implementation ---
    // (We need this to pass questions between activities)
    protected Question(Parcel in) {
        category = in.readString();
        type = in.readString();
        difficulty = in.readString();
        question = in.readString();
        correctAnswer = in.readString();
        incorrectAnswers = in.createStringArrayList();
        allAnswers = in.createStringArrayList();
        userAnswer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(difficulty);
        dest.writeString(question);
        dest.writeString(correctAnswer);
        dest.writeStringList(incorrectAnswers);
        dest.writeStringList(allAnswers);
        dest.writeString(userAnswer);
    }

    // --- Getters and Setters ---
    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public List<String> getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(List<String> allAnswers) {
        this.allAnswers = allAnswers;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}