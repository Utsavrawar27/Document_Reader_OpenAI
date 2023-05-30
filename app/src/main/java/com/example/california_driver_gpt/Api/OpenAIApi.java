package com.example.california_driver_gpt.Api;

import com.example.california_driver_gpt.Models.CompletionResult;
import com.example.california_driver_gpt.Models.CompletionRequest;
import retrofit2.http.Body;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OpenAIApi {
    @POST("v1/engines/text-davinci-003/completions")
    Call<CompletionResult> getCompletion(@Body CompletionRequest request);
}
