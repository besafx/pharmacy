package com.besafx.app.entity.enums;
public enum DepositMethod {
    FromFund("من الصندوق"),
    Manual("يدوي");
    private String name;
    DepositMethod(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public static DepositMethod findByName(String name){
        for(DepositMethod v : values()){
            if( v.getName().equals(name)){
                return v;
            }
        }
        return null;
    }
    public static DepositMethod findByValue(String value){
        for(DepositMethod v : values()){
            if( v.name().equals(value)){
                return v;
            }
        }
        return null;
    }
}
