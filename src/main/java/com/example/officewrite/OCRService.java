package com.example.officewrite;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRService {
    private final Tesseract tesseract;
    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng+hin");
    }
    public String extractText(File image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "";
        }
    }
}
