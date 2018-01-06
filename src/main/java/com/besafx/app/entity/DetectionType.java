package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class DetectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    private String descriptionArabic;

    private String descriptionEnglish;

    private Double cost;

    @JsonCreator
    public static DetectionType Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DetectionType detectionType = mapper.readValue(jsonString, DetectionType.class);
        return detectionType;
    }
}
