package com.besafx.app.entity;

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
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    private String name;

    private String address;

    private String phone;

    private String mobile;

    private String fax;

    private String email;

    private String website;

    private String commericalRegisteration;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String logo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String options;

    @JsonCreator
    public static Company Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Company company = mapper.readValue(jsonString, Company.class);
        return company;
    }
}
