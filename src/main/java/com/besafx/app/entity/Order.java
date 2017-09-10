package com.besafx.app.entity;

import com.besafx.app.entity.enums.OrderCondition;
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
@Table(name = "[Order]")
public class Order implements Serializable {

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

    @Enumerated(EnumType.STRING)
    private OrderCondition orderCondition;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "falcon")
    private Falcon falcon;

    @ManyToOne
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @JsonCreator
    public static Order Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonString, Order.class);
        return order;
    }
}
