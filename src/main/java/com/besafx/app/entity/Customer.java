package com.besafx.app.entity;

import com.besafx.app.entity.listener.CustomerListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.stream.Collectors;

@Data
@Entity
@EntityListeners(CustomerListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {

    public static final String SCREEN_NAME = "العملاء";

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    private String job;

    private String email;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Falcon> falcons = new ArrayList<>();

    @JsonCreator
    public static Customer Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(jsonString, Customer.class);
        return customer;
    }

    public List<Order> getOrders() {
        try {
            return this.falcons.stream().flatMap(falcon -> falcon.getOrders().stream()).collect(Collectors.toList());
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public Double getNetSum() {
        try {
            return this.falcons.stream().mapToDouble(falcon -> falcon.getNetSum()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPaidSum() {
        try {
            return this.falcons.stream().mapToDouble(falcon -> falcon.getPaidSum()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRemainSum() {
        try {
            return this.falcons.stream().mapToDouble(falcon -> falcon.getRemainSum()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
