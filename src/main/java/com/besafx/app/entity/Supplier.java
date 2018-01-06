package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    private String name;

    private String address;

    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    private String email;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @JsonCreator
    public static Supplier Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Supplier supplier = mapper.readValue(jsonString, Supplier.class);
        return supplier;
    }
}
