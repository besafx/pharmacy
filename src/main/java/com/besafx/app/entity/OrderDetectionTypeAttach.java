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
public class OrderDetectionTypeAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attach")
    private Attach attach;

    @ManyToOne
    @JoinColumn(name = "orderDetectionType")
    private OrderDetectionType orderDetectionType;

    @JsonCreator
    public static OrderDetectionTypeAttach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderDetectionTypeAttach orderDetectionTypeAttachment = mapper.readValue(jsonString, OrderDetectionTypeAttach.class);
        return orderDetectionTypeAttachment;
    }
}
