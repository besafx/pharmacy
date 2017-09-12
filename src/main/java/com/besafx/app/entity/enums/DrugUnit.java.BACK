package com.besafx.app.entity.enums;
public enum DrugUnit {
    Pill("الحبة"),
    Pillbox("العلبة");
    private String name;
    DrugUnit(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public static DrugUnit findByName(String name){
        for(DrugUnit v : values()){
            if( v.getName().equals(name)){
                return v;
            }
        }
        return null;
    }
    public static DrugUnit findByValue(String value){
        for(DrugUnit v : values()){
            if( v.name().equals(value)){
                return v;
            }
        }
        return null;
    }
}
