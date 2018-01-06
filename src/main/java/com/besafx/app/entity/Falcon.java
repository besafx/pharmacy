package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Falcon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long code;

    private String type;

    private Double weight;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "falcon", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @JsonCreator
    public static Falcon Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Falcon falcon = mapper.readValue(jsonString, Falcon.class);
        return falcon;
    }
}
