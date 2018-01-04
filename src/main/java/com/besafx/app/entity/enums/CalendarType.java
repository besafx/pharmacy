package com.besafx.app.entity.enums;

public enum CalendarType {
    Hijri("هجري"),
    Gregorian("ميلادي");
    private String name;

    CalendarType(String name) {
        this.name = name;
    }

    public static CalendarType findByName(String name) {
        for (CalendarType v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static CalendarType findByValue(String value) {
        for (CalendarType v : values()) {
            if (v.name().equals(value)) {
                return v;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
