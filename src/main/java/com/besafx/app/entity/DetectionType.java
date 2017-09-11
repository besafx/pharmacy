package com.besafx.app.entity;

import com.besafx.app.entity.enums.DrugUnit;
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
public class DetectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "detectionTypeSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DETECTION_TYPE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "detectionTypeSequenceGenerator")
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    private String descriptionArabic;

    private String descriptionEnglish;

    @Enumerated(EnumType.STRING)
    private DrugUnit unit;

    private Double unitCost;

    @JsonCreator
    public static DetectionType Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DetectionType detectionType = mapper.readValue(jsonString, DetectionType.class);
        return detectionType;
    }
}
