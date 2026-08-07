package com.example.officewrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class WhisperService {

    // Update these paths if necessary or read from settings
    private final String whisperBin = Paths.get("bin","main.exe").toString(); // place main.exe here
    private final String modelPath = Paths.get("models","ggml-tiny.bin").toString();

    public String transcribe(File wavFile, String lang) {
        try {
            ProcessBuilder pb = new ProcessBuilder(whisperBin, "-m", modelPath, "-f", wavFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            StringBuilder out = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) out.append(line).append("\n");
            }
            p.waitFor();
            return out.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
