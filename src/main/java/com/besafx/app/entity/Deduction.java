package com.besafx.app.entity;

import com.besafx.app.entity.enums.CalendarType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Deduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "deductionSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DETECTION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "deductionSequenceGenerator")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double amount;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "deductionType")
    private DeductionType deductionType;

    @ManyToOne
    @JoinColumn(name = "employee")
    private Employee employee;

    @JsonCreator
    public static Deduction Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Deduction deduction = mapper.readValue(jsonString, Deduction.class);
        return deduction;
    }
}
