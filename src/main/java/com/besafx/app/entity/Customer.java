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
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "customerSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "customerSequenceGenerator")
    private Long id;

    private Integer code;

    private String name;

    private String nickname;

    private String address;

    private String mobile;

    private String nationality;

    private String identityNumber;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    private String job;

    private String email;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @JsonCreator
    public static Customer Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(jsonString, Customer.class);
        return customer;
    }
}
