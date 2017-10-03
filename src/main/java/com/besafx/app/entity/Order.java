package com.besafx.app.entity;

import com.besafx.app.entity.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "[Order]")
@Component
public class Order implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Order.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSequenceGenerator")
    private Long id;

    private Integer code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer checkCode;

    private Double discount;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "falcon")
    private Falcon falcon;

    @ManyToOne
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @OrderBy(value = "drug")
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @OrderBy(value = "detectionType")
    private List<OrderDetectionType> orderDetectionTypes = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderAttach> orderAttaches = new ArrayList<>();

    public Double getDetectionTypeCostSum() {
        try {
            return this.orderDetectionTypes
                    .stream()
                    .map(OrderDetectionType::getDetectionType)
                    .mapToDouble(DetectionType::getCost)
                    .sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getNetCost() {
        try {
            Double total = getDetectionTypeCostSum();
            return total - (total * this.discount / 100);
        } catch (Exception ex) {
            return null;
        }
    }

    @JsonCreator
    public static Order Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonString, Order.class);
        return order;
    }
}
