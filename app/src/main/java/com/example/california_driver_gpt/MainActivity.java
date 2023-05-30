package com.example.california_driver_gpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.california_driver_gpt.Api.OpenAIApi;
import com.example.california_driver_gpt.Api.OpenAIHelper;
import com.example.california_driver_gpt.Models.CompletionRequest;
import com.example.california_driver_gpt.Models.CompletionResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private Button btnPickFile, btnSubmit;
    private EditText etQuestion;
    private TextView tvDocument;
    private Uri fileUri;
    private String fileType;
    private OpenAIHelper openAIHelper;
    private TextView tvAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPickFile = findViewById(R.id.btn_pick_file);
        btnSubmit = findViewById(R.id.btn_submit);
        etQuestion = findViewById(R.id.et_question);
        tvDocument = findViewById(R.id.tv_document);
        tvAnswer = findViewById(R.id.tv_answer);

        openAIHelper = new OpenAIHelper("paste your OpenAI key here");

        btnPickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile();
//                checkPermissions();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null) {
                    parseFile(fileUri, fileType);
                } else {
                    Toast.makeText(MainActivity.this, "Please choose a file first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitQuestion();
            }
        });
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), PICK_FILE_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            fileUri = data.getData();
            fileType = getContentResolver().getType(fileUri);
        }
    }

    private void parseFile(Uri fileUri, String fileType) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            String parsedText = "";

            if (fileType != null) {
                if (fileType.contains("pdf")) {
                    parsedText = PdfParser.parse(inputStream);
                } else if (fileType.contains("msword") || fileType.contains("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    parsedText = DocxParser.parse(inputStream);
                } else if (fileType.contains("text/plain")) {
                    parsedText = TxtParser.parse(inputStream);
                } else {
                    Toast.makeText(this, "Unsupported file type.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "File type could not be determined.", Toast.LENGTH_SHORT).show();
                return;
            }

            tvDocument.setText(parsedText);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitQuestion() {
        String document = tvDocument.getText().toString();
        String question = etQuestion.getText().toString();

        if (question.isEmpty()) {
            Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
            return;
        }
        // Truncate the document to fit within the model's token limit
        int maxDocumentTokens = 2500; // Adjust this value as needed
        String truncatedDocument = OpenAIHelper.truncateToTokens(document, maxDocumentTokens);

        String prompt = "Document:\n" + truncatedDocument + "\n\nQuestion: " + question + "\nAnswer:";
        CompletionRequest request = new CompletionRequest(prompt, 500); // Customize the request as needed
        GetAnswerTask getAnswerTask = new GetAnswerTask(this, openAIHelper, new GetAnswerTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                tvAnswer.setText(result);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error getting answer from API", Toast.LENGTH_SHORT).show();
            }
        });
        getAnswerTask.execute(request);
    }
}