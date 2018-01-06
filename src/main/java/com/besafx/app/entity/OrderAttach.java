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
public class OrderAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attach")
    private Attach attach;

    @ManyToOne
    @JoinColumn(name = "[order]")
    private Order order;

    @JsonCreator
    public static OrderAttach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderAttach attachment = mapper.readValue(jsonString, OrderAttach.class);
        return attachment;
    }
}
