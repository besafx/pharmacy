package com.besafx.app.entity;

import com.besafx.app.entity.enums.DrugUnit;
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
public class TransactionSellDetection implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "transactionSellDetectionSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TRANSACTION_SELL_DETECTION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "transactionSellDetectionSequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DrugUnit unit;

    private Double unitCost;

    private Double discount;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @JoinColumn(name = "detectionType")
    @ManyToOne
    private DetectionType detectionType;

    @JoinColumn(name = "billSellDetection")
    @ManyToOne
    private BillSellDetection billSellDetection;

    @JsonCreator
    public static TransactionSellDetection Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TransactionSellDetection transactionSellDetection = mapper.readValue(jsonString, TransactionSellDetection.class);
        return transactionSellDetection;
    }
}
