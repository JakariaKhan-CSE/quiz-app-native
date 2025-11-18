package com.mind.spark;


import com.mind.spark.models.Question;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaResponse {

    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("results")
    private List<Question> results;

    public List<Question> getResults() {
        return results;
    }
}