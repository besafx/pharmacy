package com.besafx.app.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class JSONConverter {

    private static ObjectMapper mapper;

    @PostConstruct
    public void init(){
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String toString(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Object> T toObject(String jsonInString, Class<T> type){
        try {
            return type.cast(mapper.readValue(jsonInString, type));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
