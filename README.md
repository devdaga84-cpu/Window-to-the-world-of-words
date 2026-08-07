# Window to the World of Words - OfficeWrite Java Prototype

This repository contains the Java/JavaFX prototype for "Window to the World of Words" — an offline-first word processor with speech-to-text (Whisper), KrutiDev conversion for Hindi, and OCR (Tesseract via Tess4J).

Contents pushed in this commit:
- src/main/java/com/example/officewrite: MainApp, Recorder, WhisperService, KrutiDevConverter, OCRService, SettingsUtil, SettingsDialog
- pom.xml
- README.md (this file)

What is NOT included in the repo (you must download separately):
- Whisper binary (whisper.cpp build main.exe) — place in project-root/bin/main.exe
- GGML model (ggml-tiny.bin) — place in project-root/models/ggml-tiny.bin
- Tesseract tessdata (eng.traineddata, hin.traineddata) if you don't have Tesseract installed system-wide — place tessdata/ directory in project root

Quick setup and run (Windows, JDK 17+, Maven):
1) Ensure JDK 17+ and Maven are installed.
2) Place whisper binary and model as described above.
3) (Optional) Put tessdata/ with eng and hin traineddata in project root or install Tesseract and set TESSDATA_PREFIX.
4) Build and run:
   mvn clean package
   mvn javafx:run

Usage notes:
- Click the microphone toggle to start/stop recording. The app records 16kHz mono WAV and calls whisper binary to transcribe.
- Settings allow enabling "Insert Hindi as KrutiDev". When enabled and "Hindi" language selected, KrutiDev conversion runs before insertion.
- The repo intentionally does NOT include large binaries/models. See README for download links and recommendations.

Next steps recommended:
- Tune KrutiDev mapping with real transcripts.
- Replace HTMLEditor with RichTextFX for better caret insertion and per-style font control if needed.
- Package the app with jpackage and set an .ico for installer and exe.

If you want me to push binaries or prepare a release installer, tell me and I will provide instructions and next steps.
