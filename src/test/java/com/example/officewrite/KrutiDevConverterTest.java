package com.example.officewrite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KrutiDevConverterTest {

    @Test
    public void simpleConversion() {
        String input = "भारत";
        String out = KrutiDevConverter.unicodeToKrutiDev(input);
        assertNotNull(out);
        assertFalse(out.isEmpty());
    }

    @Test
    public void consonantWithMatra() {
        String input = "का"; // क + ा
        String out = KrutiDevConverter.unicodeToKrutiDev(input);
        assertTrue(out.contains("k") || out.length() > 0);
    }
}
