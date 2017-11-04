package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@Table
public class OrderReceipt implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OrderReceipt.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderReceiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderReceiptSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "[order]")
    private Order order;

    @JsonCreator
    public static OrderReceipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderReceipt orderReceipt = mapper.readValue(jsonString, OrderReceipt.class);
        return orderReceipt;
    }
}
