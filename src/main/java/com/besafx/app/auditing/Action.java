package com.besafx.app.auditing;


public enum Action {

    INSERTED("اضافة"),
    UPDATED("تعديل"),
    DELETED("حذف");

    private final String name;

    Action(String value) {
        this.name = value;
    }

    public String value() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
