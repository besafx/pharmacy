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
public class OrderDetectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderDetectionTypeSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_DETECTION_TYPE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderDetectionTypeSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "[order]")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "detectionType")
    private DetectionType detectionType;

    @JsonCreator
    public static OrderDetectionType Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderDetectionType orderDetectionType = mapper.readValue(jsonString, OrderDetectionType.class);
        return orderDetectionType;
    }
}
