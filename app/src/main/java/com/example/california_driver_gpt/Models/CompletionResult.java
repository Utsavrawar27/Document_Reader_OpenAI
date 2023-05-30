package com.example.california_driver_gpt.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletionResult {
    @SerializedName("choices")
    private List<Completion> completions;

    public CompletionResult(List<Completion> completions) {
        this.completions = completions;
    }

    public List<Completion> getCompletions() {
        return completions;
    }

    public void setCompletions(List<Completion> completions) {
        this.completions = completions;
    }

    public static class Completion {
        private String text;

        public Completion(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @Override
    public String toString() {
        return "CompletionResult{" +
                "completions=" + completions +
                '}';
    }

}
