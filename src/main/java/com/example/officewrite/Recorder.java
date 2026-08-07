package com.example.officewrite;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Recorder {
    private TargetDataLine line;
    private Thread writerThread;
    private Path wavFile;

    // Start recording and return path where WAV will be written
    public Path startRecording() throws Exception {
        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false); // 16kHz, 16bit, mono, little-endian
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) throw new LineUnavailableException("Microphone not supported for 16kHz mono");
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        wavFile = Files.createTempFile("officewrite_rec_", ".wav");
        writerThread = new Thread(() -> {
            try (AudioInputStream ais = new AudioInputStream(line)) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Recorder-Writer");
        writerThread.start();
        return wavFile;
    }

    // Stop and return final WAV file path
    public Path stopRecordingAndGetFile() throws Exception {
        if (line != null) {
            line.stop();
            line.flush();
            line.close();
            line = null;
        }
        if (writerThread != null) {
            writerThread.join(2000);
            writerThread = null;
        }
        return wavFile;
    }
}
