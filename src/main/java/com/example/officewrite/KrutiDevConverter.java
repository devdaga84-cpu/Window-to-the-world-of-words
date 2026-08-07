package com.example.officewrite;

import java.util.HashMap;
import java.util.Map;

public class KrutiDevConverter {
    private static final Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put("अ", "v");
        MAP.put("आ", "vk");
        MAP.put("इ", "s");
        MAP.put("ई", "S");
        MAP.put("उ", "u");
        MAP.put("ऊ", "U");
        MAP.put("ए", "b");
        MAP.put("ऐ", "B");
        MAP.put("ओ", "o");
        MAP.put("औ", "O");
        MAP.put("ऋ", "Z");

        MAP.put("क", "d");
        MAP.put("ख", "[");
        MAP.put("ग", "x");
        MAP.put("घ", "X");
        MAP.put("ङ", "M");
        MAP.put("च", "V");
        MAP.put("छ", "Y");
        MAP.put("ज", "t");
        MAP.put("झ", "T");
        MAP.put("ञ", "N");
        MAP.put("ट", "w");
        MAP.put("ठ", "Q");
        MAP.put("ड", "W");
        MAP.put("ढ", "q");
        MAP.put("ण", "R");
        MAP.put("त", "r");
        MAP.put("थ", "e");
        MAP.put("द", "j");
        MAP.put("ध", "E");
        MAP.put("न", "n");
        MAP.put("প", "i");
        MAP.put("ফ", "I");
        MAP.put("ব", "c");
        MAP.put("ভ", "C");
        MAP.put("ম", "m");
        MAP.put("য", "y");
        MAP.put("র", "k");
        MAP.put("ল", "l");
        MAP.put("व", "o");
        MAP.put("শ", "p");
        MAP.put("ষ", "P");
        MAP.put("স", "g");
        MAP.put("হ", "H");

        MAP.put("क़", "d`"); MAP.put("ख़", "['"); MAP.put("ग़", "x`"); MAP.put("ज़","t`"); MAP.put("फ़","i`");

        MAP.put("ा", "k");
        MAP.put("ि", "f");
        MAP.put("ी", "ff");
        MAP.put("ु", "p");
        MAP.put("ू", "P");
        MAP.put("े", "g");
        MAP.put("ै", "~");
        MAP.put("ो", "b");
        MAP.put("ौ", "B");
        MAP.put("ं", "vks");
        MAP.put("ँ", "Z");
        MAP.put("ः", ":");

        MAP.put("्", ";");
        MAP.put("।", ".");
        MAP.put("॥", "..");
        MAP.put("०","0"); MAP.put("১","1"); MAP.put("২","2"); MAP.put("৩","3");
        MAP.put("৪","4"); MAP.put("৫","5"); MAP.put("৬","6"); MAP.put("৭","7");
        MAP.put("৮","8"); MAP.put("৯","9");
    }

    public static String unicodeToKrutiDev(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder out = new StringBuilder();
        char[] chars = input.toCharArray();
        int i = 0;
        while (i < chars.length) {
            String ch = String.valueOf(chars[i]);

            if (isConsonant(ch)) {
                String next = (i + 1 < chars.length) ? String.valueOf(chars[i + 1]) : null;

                if ("ि".equals(next)) {
                    String m = MAP.get("ि");
                    if (m != null) out.append(m);
                    String cMap = MAP.get(ch);
                    out.append(cMap != null ? cMap : ch);
                    i += 2;
                    continue;
                }

                if ("्".equals(next)) {
                    String cMap = MAP.get(ch);
                    out.append(cMap != null ? cMap : ch);
                    String vMap = MAP.get("्");
                    out.append(vMap != null ? vMap : "");
                    i += 1;
                    i++;
                    continue;
                }

                if (next != null && isMatra(next)) {
                    String cMap = MAP.get(ch);
                    out.append(cMap != null ? cMap : ch);
                    String m = MAP.get(next);
                    if (m != null) out.append(m);
                    i += 2;
                    continue;
                }

                String cMap = MAP.get(ch);
                out.append(cMap != null ? cMap : ch);
                i++;
                continue;
            }

            if (MAP.containsKey(ch)) {
                out.append(MAP.get(ch));
                i++;
                continue;
            }

            out.append(ch);
            i++;
        }

        return out.toString();
    }

    private static boolean isConsonant(String s) {
        if (s == null || s.isEmpty()) return false;
        int cp = s.codePointAt(0);
        return (cp >= 0x0915 && cp <= 0x0939) || (cp == 0x0958) || (cp == 0x0959) || (cp == 0x095A) || (cp == 0x095B);
    }

    private static boolean isMatra(String s) {
        if (s == null || s.isEmpty()) return false;
        int cp = s.codePointAt(0);
        return (cp >= 0x093E && cp <= 0x094C) || cp == 0x0902 || cp == 0x0901 || cp == 0x0903;
    }
}
