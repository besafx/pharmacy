package com.besafx.app.entity.enums;
public enum PersonType {
    Customer("عميل"),
    Doctor("طبيب"),
    Employee("موظف");
    private String name;
    PersonType(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public static PersonType findByName(String name){
        for(PersonType v : values()){
            if( v.getName().equals(name)){
                return v;
            }
        }
        return null;
    }
    public static PersonType findByValue(String value){
        for(PersonType v : values()){
            if( v.name().equals(value)){
                return v;
            }
        }
        return null;
    }
}
