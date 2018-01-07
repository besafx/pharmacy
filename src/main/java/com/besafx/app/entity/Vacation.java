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
public class Vacation implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "vacationSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "VACATION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "vacationSequenceGenerator")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Integer days;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "vacationType")
    private VacationType vacationType;

    @ManyToOne
    @JoinColumn(name = "employee")
    private Employee employee;

    @JsonCreator
    public static Vacation Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Vacation vacation = mapper.readValue(jsonString, Vacation.class);
        return vacation;
    }
}
