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

@Data
@Entity
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "salarySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DETECTION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "salarySequenceGenerator")
    private Long id;

    @Column(length = 2)
    private Integer month;

    @Column(length = 4)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private CalendarType calendarType;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "employee")
    private Employee employee;

    @JsonCreator
    public static Salary Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Salary salary = mapper.readValue(jsonString, Salary.class);
        return salary;
    }
}
