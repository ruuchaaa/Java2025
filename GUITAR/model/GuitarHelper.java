package com.ap.GUITAR.model;
public class GuitarHelper {
    public static int getStringCountByLevel(String level) {
        switch (level.toLowerCase()) {
            case "beginner": return 6;
            case "intermediate": return 12;
            default: return 0;
        }
    }
}