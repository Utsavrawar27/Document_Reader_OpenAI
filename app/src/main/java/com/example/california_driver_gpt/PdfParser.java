package com.example.california_driver_gpt;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class PdfParser {
    public static String parse(InputStream inputStream) throws IOException {
        PDDocument document = null;
        String parsedText = "";

        try {
            document = PDDocument.load(inputStream);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            parsedText = pdfTextStripper.getText(document);
        } finally {
            if (document != null) {
                document.close();
            }
        }

        return parsedText;
    }
}
