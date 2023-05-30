package com.example.california_driver_gpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TxtParser {
    public static String parse(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder parsedText = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            parsedText.append(line);
            parsedText.append("\n");
        }

        return parsedText.toString();
    }
}
