package com.example.officewrite;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SettingsUtil {
    private static final Path SETTINGS_FILE = Path.of(System.getProperty("user.home"), ".officewrite_settings.properties");

    public static Properties load() {
        Properties p = new Properties();
        p.setProperty("shortcut.en","Ctrl+Numpad1");
        p.setProperty("shortcut.hi","Ctrl+Numpad2");
        p.setProperty("shortcut.mic","F4");
        p.setProperty("kruti.enabled","false");
        try {
            if (Files.exists(SETTINGS_FILE)) {
                try (InputStream in = Files.newInputStream(SETTINGS_FILE)) { p.load(in); }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return p;
    }

    public static void save(Properties p) {
        try {
            try (OutputStream out = Files.newOutputStream(SETTINGS_FILE)) { p.store(out, "OfficeWrite Settings"); }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
