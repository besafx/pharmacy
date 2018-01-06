package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class TeamRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameArabic;

    private String nameEnglish;

    private String value;

    private boolean selected;

    @JsonCreator
    public static TeamRule Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TeamRule teamRule = mapper.readValue(jsonString, TeamRule.class);
        return teamRule;
    }
}
