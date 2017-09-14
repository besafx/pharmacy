package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "depositSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DEPOSIT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "depositSequenceGenerator")
    private Long id;

    private Long code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double amount;

    private String depositor;

    private String checkNumber;

    private String checkBankName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "bank")
    private Bank bank;

    @JsonCreator
    public static Deposit Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Deposit deposit = mapper.readValue(jsonString, Deposit.class);
        return deposit;
    }
}
