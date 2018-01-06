package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class DrugUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer factor;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JsonCreator
    public static DrugUnit Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DrugUnit drugUnit = mapper.readValue(jsonString, DrugUnit.class);
        return drugUnit;
    }
}
