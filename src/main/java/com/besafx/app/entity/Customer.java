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
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    private String name;

    private String nickname;

    private String address;

    private String mobile;

    private String nationality;

    private String identityNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    private String job;

    private String email;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @OneToMany(mappedBy = "customer")
    private List<Falcon> falcons = new ArrayList<>();

    @JsonCreator
    public static Customer Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(jsonString, Customer.class);
        return customer;
    }
}
