package com.example.officewrite;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Properties;

public class SettingsDialog {

    public static void show(Stage owner, Properties settings) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Settings");

        GridPane grid = new GridPane();
        grid.setHgap(8); grid.setVgap(8); grid.setPadding(new Insets(12));

        Label l1 = new Label("English shortcut:");
        TextField tEn = new TextField(settings.getProperty("shortcut.en","Ctrl+Numpad1"));
        Label l2 = new Label("Hindi shortcut:");
        TextField tHi = new TextField(settings.getProperty("shortcut.hi","Ctrl+Numpad2"));
        Label l3 = new Label("Mic shortcut:");
        TextField tMic = new TextField(settings.getProperty("shortcut.mic","F4"));

        CheckBox krutiCb = new CheckBox("Insert Hindi as KrutiDev (legacy)");
        krutiCb.setSelected(Boolean.parseBoolean(settings.getProperty("kruti.enabled","false")));

        grid.add(l1,0,0); grid.add(tEn,1,0);
        grid.add(l2,0,1); grid.add(tHi,1,1);
        grid.add(l3,0,2); grid.add(tMic,1,2);
        grid.add(krutiCb,0,3,2,1);

        HBox actions = new HBox(8);
        actions.setPadding(new Insets(8,0,0,0));
        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        actions.getChildren().addAll(save, cancel);

        save.setOnAction(e -> {
            settings.setProperty("shortcut.en", tEn.getText().trim());
            settings.setProperty("shortcut.hi", tHi.getText().trim());
            settings.setProperty("shortcut.mic", tMic.getText().trim());
            settings.setProperty("kruti.enabled", Boolean.toString(krutiCb.isSelected()));
            SettingsUtil.save(settings);
            dialog.close();
        });
        cancel.setOnAction(e -> dialog.close());

        VBox root = new VBox(grid, actions);
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
