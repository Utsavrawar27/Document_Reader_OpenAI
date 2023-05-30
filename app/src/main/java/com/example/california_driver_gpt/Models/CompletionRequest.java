package com.example.california_driver_gpt.Models;

import com.google.gson.annotations.SerializedName;

public class CompletionRequest {
    @SerializedName("prompt")
    private String prompt;

    @SerializedName("max_tokens")
    private int maxTokens;

    public CompletionRequest(String prompt, int maxTokens) {
        this.prompt = prompt;
        this.maxTokens = maxTokens;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

}
