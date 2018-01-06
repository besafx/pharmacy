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
public class BillSellReceipt implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(BillSellReceipt.class);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fund")
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "billSell")
    private BillSell billSell;

    @JsonCreator
    public static BillSellReceipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillSellReceipt billSellReceipt = mapper.readValue(jsonString, BillSellReceipt.class);
        return billSellReceipt;
    }
}
