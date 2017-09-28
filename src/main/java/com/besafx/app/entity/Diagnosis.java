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
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Diagnosis implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "diagnosisSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DIAGNOSIS_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "diagnosisSequenceGenerator")
    private Long id;

    private Integer code;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String usage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double quantity;

    @JoinColumn(name = "drug")
    @ManyToOne
    @OrderBy(value = "code")
    private Drug drug;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JoinColumn(name = "[order]")
    @ManyToOne
    private Order order;

    @JsonCreator
    public static Diagnosis Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Diagnosis diagnosis = mapper.readValue(jsonString, Diagnosis.class);
        return diagnosis;
    }
}
