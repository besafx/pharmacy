package com.besafx.app.ws;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class Notification {

    private String code;

    private String title;

    private String message;

    private Date date;

    private String type;

    private String icon;

    private String layout;

    private String sender;

    private String receiver;

    @JsonCreator
    public static Notification Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Notification notify = mapper.readValue(jsonString, Notification.class);
        return notify;
    }
}
