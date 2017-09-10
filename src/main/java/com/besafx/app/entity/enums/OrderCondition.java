package com.besafx.app.entity.enums;
public enum OrderCondition {
    Pending("تحت التنفيذ"),
    Processing("تحت الفحص"),
    Done("مكتمل"),
    Canceled("ملغي");
    private String name;
    OrderCondition(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public static OrderCondition findByName(String name){
        for(OrderCondition v : values()){
            if( v.getName().equals(name)){
                return v;
            }
        }
        return null;
    }
    public static OrderCondition findByValue(String value){
        for(OrderCondition v : values()){
            if( v.name().equals(value)){
                return v;
            }
        }
        return null;
    }
}
