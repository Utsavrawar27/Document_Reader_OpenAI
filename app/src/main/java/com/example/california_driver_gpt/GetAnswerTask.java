package com.example.california_driver_gpt;

import android.os.AsyncTask;
import android.util.Log;

import com.example.california_driver_gpt.Api.OpenAIHelper;
import com.example.california_driver_gpt.Models.CompletionRequest;
import com.example.california_driver_gpt.Models.CompletionResult;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class GetAnswerTask extends AsyncTask<CompletionRequest, Void, String> {

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);

        void onError(Exception e);
    }

    private final OpenAIHelper openAIHelper;
    private final WeakReference<MainActivity> activityReference;
    private final OnTaskCompleted callback;
    private Exception exception;

    public GetAnswerTask(MainActivity context, OpenAIHelper openAIHelper, OnTaskCompleted callback) {
        this.activityReference = new WeakReference<>(context);
        this.openAIHelper = openAIHelper;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(CompletionRequest... requests) {
        try {
            CompletionResult result = openAIHelper.getCompletion(requests[0]);
            Log.d("API_RESPONSE", result.toString());
            if (result.getCompletions() == null || result.getCompletions().isEmpty()) {
                throw new Exception("No completions found in the result. API response: " + result.toString());
            }
            return result.getCompletions().get(0).getText();
        } catch (IOException e) {
            exception = e;
            return null;
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String answer) {
        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (exception != null) {
            callback.onError(exception);
        } else {
            callback.onTaskCompleted(answer);
        }
    }

}
