package com.example.officewrite;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

public class MainApp extends Application {

    private HTMLEditor editor;
    private ToggleButton micBtn;
    private ComboBox<String> langCombo;
    private Recorder recorder;
    private WhisperService whisperService;
    private Properties settings;

    @Override
    public void start(Stage stage) throws Exception {
        // load settings (shortcuts, kruti toggle etc.) — simple Properties file
        settings = SettingsUtil.load();

        // Services
        recorder = new Recorder();
        whisperService = new WhisperService(); // needs path config internally or via settings

        BorderPane root = new BorderPane();
        root.setTop(createRibbon(stage));
        root.setLeft(createIconPane());
        root.setCenter(createEditorPane());
        root.setBottom(createStatusBar());

        Scene scene = new Scene(root, 1100, 760);
        configureShortcuts(scene);
        stage.setScene(scene);
        stage.setTitle("OfficeWrite (Java) - Prototype");
        stage.show();
    }

    private Node createRibbon(Stage stage) {
        HBox ribbon = new HBox(10);
        ribbon.setPadding(new Insets(8));
        ribbon.setStyle("-fx-background-color: linear-gradient(to right, #ff7043, #f06292); -fx-text-fill:white;");

        Label title = new Label("OfficeWrite");
        title.setStyle("-fx-font-weight:bold; -fx-text-fill: white; -fx-font-size: 14px;");

        Button newBtn = new Button("New");
        newBtn.setOnAction(e -> editor.setHtmlText("<p></p>"));

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> { /* implement save to docx/pdf later */ });

        langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "Hindi (KrutiDev)", "Auto-detect");
        langCombo.getSelectionModel().select("English");

        micBtn = new ToggleButton("🎤 Off");
        micBtn.setOnAction(ev -> {
            if (micBtn.isSelected()) startRecordingAndTranscribe();
            else stopRecordingAndTranscribe();
        });

        Button ocrBtn = new Button("OCR (Image → Text)");
        ocrBtn.setOnAction(ev -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp"));
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                OCRService ocr = new OCRService();
                new Thread(() -> {
                    String txt = ocr.extractText(file);
                    Platform.runLater(() -> insertTextAtCursor(txt));
                }).start();
            }
        });

        Button settingsBtn = new Button("Settings");
        settingsBtn.setOnAction(e -> SettingsDialog.show(stage, settings));

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        ribbon.getChildren().addAll(title, newBtn, saveBtn, new Label("Language:"), langCombo, ocrBtn, spacer, micBtn, settingsBtn);
        return ribbon;
    }

    private Node createIconPane() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(8));
        box.setPrefWidth(100);
        // placeholder icons — will replace with user photos converted to icons later
        for (int i=0;i<3;i++){
            Button b = new Button();
            b.setPrefSize(60,60);
            b.setStyle("-fx-background-color: white; -fx-border-radius: 6; -fx-background-radius: 6;");
            box.getChildren().add(b);
        }
        return box;
    }

    private Node createEditorPane() {
        editor = new HTMLEditor();
        editor.setHtmlText("<p>Yah sample document area hai. Yahan bol kar ya keyboard se likh kar aap text daal sakte hain.</p>");
        return editor;
    }

    private Node createStatusBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(6));
        Label status = new Label("Ready");
        bar.getChildren().add(status);
        return bar;
    }

    private void startRecordingAndTranscribe() {
        micBtn.setText("🎤 On");
        try {
            Path wavPath = recorder.startRecording(); // returns path where it will write
        } catch (Exception e) {
            e.printStackTrace();
            micBtn.setSelected(false);
            micBtn.setText("🎤 Off");
        }
    }

    private void stopRecordingAndTranscribe() {
        micBtn.setText("🎤 Off");
        try {
            Path wav = recorder.stopRecordingAndGetFile();
            new Thread(() -> {
                String langOpt = langCombo.getValue();
                String langFlag = langOpt.startsWith("Hindi") ? "hi" : (langOpt.startsWith("English") ? "en" : "auto");
                String result = whisperService.transcribe(wav.toFile(), langFlag);
                if (result != null && !result.isBlank()) {
                    boolean insertKruti = Boolean.parseBoolean(settings.getProperty("kruti.enabled","false"));
                    String finalText = result;
                    if (insertKruti && langFlag.equals("hi")) {
                        finalText = KrutiDevConverter.unicodeToKrutiDev(result);
                    }
                    final String textToInsert = finalText;
                    Platform.runLater(() -> insertTextAtCursor(textToInsert));
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTextAtCursor(String text) {
        String prev = editor.getHtmlText();
        editor.setHtmlText(prev + "<p>" + escapeHtml(text) + "</p>");
    }

    private String escapeHtml(String in){
        return in.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br/>");
    }

    private void configureShortcuts(Scene scene) {
        String en = settings.getProperty("shortcut.en","Ctrl+Numpad1");
        String hi = settings.getProperty("shortcut.hi","Ctrl+Numpad2");
        String mic = settings.getProperty("shortcut.mic","F4");

        scene.getAccelerators().put(KeyCombination.keyCombination(en), () -> {
            langCombo.getSelectionModel().select("English");
        });
        scene.getAccelerators().put(KeyCombination.keyCombination(hi), () -> {
            langCombo.getSelectionModel().select("Hindi (KrutiDev)");
        });
        scene.getAccelerators().put(KeyCombination.keyCombination(mic), () -> {
            micBtn.setSelected(!micBtn.isSelected());
            if (micBtn.isSelected()) startRecordingAndTranscribe(); else stopRecordingAndTranscribe();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
