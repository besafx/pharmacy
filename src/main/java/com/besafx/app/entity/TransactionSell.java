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
public class TransactionSell implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "transactionSellSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TRANSACTION_SELL_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "transactionSellSequenceGenerator")
    private Long id;

    private Integer code;

    private Double unitCost;

    private Double quantity;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JoinColumn(name = "transactionBuy")
    @ManyToOne
    private TransactionBuy transactionBuy;

    @JoinColumn(name = "billSell")
    @ManyToOne
    private BillSell billSell;

    @JsonCreator
    public static TransactionSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TransactionSell transactionSell = mapper.readValue(jsonString, TransactionSell.class);
        return transactionSell;
    }
}
