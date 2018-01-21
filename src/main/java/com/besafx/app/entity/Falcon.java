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

    @GenericGenerator(
            name = "falconSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "FALCON_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "falconSequenceGenerator")
    private Long id;

    private Long code;

    private String type;

    private Double weight;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

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

    public Double getNetSum() {
        try {
            return this.orders.stream().mapToDouble(order -> order.getNetCost()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPaidSum() {
        try {
            return this.orders.stream().mapToDouble(order -> order.getPaid()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRemainSum() {
        try {
            return this.orders.stream().mapToDouble(order -> order.getRemain()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
