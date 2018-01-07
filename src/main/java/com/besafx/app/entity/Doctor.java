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
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "doctorSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DOCTOR_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "doctorSequenceGenerator")
    private Long id;

    private Integer code;

    @JoinColumn(name = "person")
    @OneToOne
    private Person person;

    @JsonCreator
    public static Doctor Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Doctor doctor = mapper.readValue(jsonString, Doctor.class);
        return doctor;
    }
}
