package com.mind.spark.network;

import com.mind.spark.TriviaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api.php")
    Call<TriviaResponse> getQuestions(
            @Query("amount") int amount,
            @Query("category") int category,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );

    // Use this if no category is selected
    @GET("api.php")
    Call<TriviaResponse> getQuestions(
            @Query("amount") int amount,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );
}