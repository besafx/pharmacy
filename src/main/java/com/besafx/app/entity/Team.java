package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String authorities;

    @OneToMany(mappedBy = "team")
    private List<Person> persons = new ArrayList<>();

    @JsonCreator
    public static Team Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Team team = mapper.readValue(jsonString, Team.class);
        return team;
    }
}
