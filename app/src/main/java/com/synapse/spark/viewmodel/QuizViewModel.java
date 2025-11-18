package com.synapse.spark.viewmodel;


import android.text.Html;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.synapse.spark.TriviaResponse;
import com.synapse.spark.models.Question;
import com.synapse.spark.network.ApiClient;
import com.synapse.spark.network.ApiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends ViewModel {

    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Question>> getQuestionList() {
        return questionList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchQuestions(int amount, int category, String difficulty) {
        isLoading.setValue(true);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<TriviaResponse> call;
        if (category == 0) { // 0 will be "Any Category"
            call = apiService.getQuestions(amount, difficulty.toLowerCase(), "multiple");
        } else {
            call = apiService.getQuestions(amount, category, difficulty.toLowerCase(), "multiple");
        }

        call.enqueue(new Callback<TriviaResponse>() {
            @Override
            public void onResponse(Call<TriviaResponse> call, Response<TriviaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body().getResults();
                    processQuestions(questions); // Process HTML entities and shuffle answers
                    questionList.setValue(questions);
                } else {
                    error.setValue("Failed to fetch questions. Code: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<TriviaResponse> call, Throwable t) {
                error.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    // Decode HTML and shuffle answers
    private void processQuestions(List<Question> questions) {
        for (Question q : questions) {
            // Decode HTML entities (e.g., &quot; becomes ")
            q.getQuestion().replace(Html.fromHtml(q.getQuestion()).toString(), q.getQuestion());
            q.getCorrectAnswer().replace(Html.fromHtml(q.getCorrectAnswer()).toString(), q.getCorrectAnswer());
            for (int i = 0; i < q.getIncorrectAnswers().size(); i++) {
                q.getIncorrectAnswers().set(i, Html.fromHtml(q.getIncorrectAnswers().get(i)).toString());
            }

            // Combine and shuffle answers
            List<String> allAnswers = new ArrayList<>(q.getIncorrectAnswers());
            allAnswers.add(q.getCorrectAnswer());
            Collections.shuffle(allAnswers);
            q.setAllAnswers(allAnswers);
        }
    }
}