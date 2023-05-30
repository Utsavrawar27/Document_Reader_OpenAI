package com.example.california_driver_gpt.Api;

import com.example.california_driver_gpt.Models.CompletionRequest;
import com.example.california_driver_gpt.Models.CompletionResult;

import java.io.IOException;
import java.util.StringTokenizer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenAIHelper {
    private static final String API_BASE_URL = "https://api.openai.com/";
    private final OpenAIApi openAIApi;
    private final String apiKey;

    public OpenAIHelper(String apiKey) {
        this.apiKey = apiKey;
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + apiKey)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        openAIApi = retrofit.create(OpenAIApi.class);
    }

    // Add this method to your OpenAIHelper class
    public static String truncateToTokens(String text, int maxTokens) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        StringBuilder truncatedText = new StringBuilder();
        int tokenCount = 0;

        while (tokenizer.hasMoreTokens() && tokenCount < maxTokens) {
            String token = tokenizer.nextToken();
            truncatedText.append(token).append(" ");
            tokenCount++;
        }

        return truncatedText.toString().trim();
    }

    public CompletionResult getCompletion(CompletionRequest request) throws IOException {
        Response<CompletionResult> response = openAIApi.getCompletion(request).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException("Error getting completion: " + response.errorBody().string());
        }
    }
}
