package com.example.officewrite;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WhisperService {

    // Update these paths if necessary or read from settings
    private final String whisperBin = Path.of("bin","main.exe").toString(); // place main.exe here
    private final String modelPath = Path.of("models","ggml-tiny.bin").toString();

    public String transcribe(File wavFile, String lang) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            Files.createDirectories(Path.of("logs"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path logFile = Path.of("logs","whisper_" + timestamp + ".log");
        Path errFile = Path.of("logs","whisper_" + timestamp + ".err");

        ProcessBuilder pb = new ProcessBuilder(whisperBin, "-m", modelPath, "-f", wavFile.getAbsolutePath());
        pb.redirectErrorStream(false);
        try {
            Process p = pb.start();

            // capture stdout
            Thread outThread = new Thread(() -> {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                     BufferedWriter bw = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                } catch (IOException ex) { ex.printStackTrace(); }
            }, "whisper-stdout");
            outThread.start();

            // capture stderr
            Thread errThread = new Thread(() -> {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                     BufferedWriter bw = Files.newBufferedWriter(errFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                } catch (IOException ex) { ex.printStackTrace(); }
            }, "whisper-stderr");
            errThread.start();

            p.waitFor();
            outThread.join(1000);
            errThread.join(1000);

            // Read log file and return content
            StringBuilder out = new StringBuilder();
            if (Files.exists(logFile)) {
                try (BufferedReader br = Files.newBufferedReader(logFile)) {
                    String line;
                    while ((line = br.readLine()) != null) out.append(line).append("\n");
                }
            }

            String result = out.toString().trim();
            // basic cleanup: remove progress/status lines if any (simple heuristic)
            result = result.replaceAll("(?m)^\[[^]]*\]$", "").trim();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Files.writeString(errFile, e.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) { ex.printStackTrace(); }
            return "";
        }
    }
}
