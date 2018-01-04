package com.besafx.app.entity.enums;

public enum ReceiptType {
    In("سند قبض"),
    Out("سند صرف");
    private String name;

    ReceiptType(String name) {
        this.name = name;
    }

    public static ReceiptType findByName(String name) {
        for (ReceiptType v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public static ReceiptType findByValue(String value) {
        for (ReceiptType v : values()) {
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
