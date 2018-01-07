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
public class DeductionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "deductionTypeSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DEDUCTION_TYPE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "deductionTypeSequenceGenerator")
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    @JsonCreator
    public static DeductionType Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeductionType deductionType = mapper.readValue(jsonString, DeductionType.class);
        return deductionType;
    }
}
