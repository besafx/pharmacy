package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@EntityListeners(MyEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company implements Serializable {

    public static final String SCREEN_NAME = "الشركة";

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "companySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "COMPANY_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "companySequenceGenerator")
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
