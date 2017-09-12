package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class DrugUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "drugUnitSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DRUG_UNIT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "drugUnitSequenceGenerator")
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
