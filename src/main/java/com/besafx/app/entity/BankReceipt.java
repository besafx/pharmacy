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
public class BankReceipt implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(BankReceipt.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "bankReceiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BANK_RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "bankReceiptSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "bank")
    private Bank bank;

    @JsonCreator
    public static BankReceipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BankReceipt bankReceipt = mapper.readValue(jsonString, BankReceipt.class);
        return bankReceipt;
    }
}
