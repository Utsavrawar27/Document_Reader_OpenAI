package com.example.california_driver_gpt;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DocxParser {
    public static String parse(InputStream inputStream) throws IOException {
        XWPFDocument document = new XWPFDocument(inputStream);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        StringBuilder parsedText = new StringBuilder();
        for (XWPFParagraph paragraph : paragraphs) {
            parsedText.append(paragraph.getText());
            parsedText.append("\n");
        }

        return parsedText.toString();
    }
}
