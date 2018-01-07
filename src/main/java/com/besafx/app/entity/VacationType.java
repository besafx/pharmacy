package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class VacationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "vacationTypeSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "VACATION_TYPE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "vacationTypeSequenceGenerator")
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    private Integer limitInDays;

    @JsonCreator
    public static VacationType Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        VacationType vacationType = mapper.readValue(jsonString, VacationType.class);
        return vacationType;
    }
}
