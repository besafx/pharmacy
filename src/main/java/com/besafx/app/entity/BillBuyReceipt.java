package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.entity.listener.BillBuyReceiptListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@EntityListeners(BillBuyReceiptListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillBuyReceipt implements Serializable {

    public static final String SCREEN_NAME = "دفعات السداد لفواتير الشراء";

    private static final Logger log = LoggerFactory.getLogger(BillBuyReceipt.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billBuyReceiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_BUY_RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billBuyReceiptSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "billBuy")
    private BillBuy billBuy;

    @JsonCreator
    public static BillBuyReceipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillBuyReceipt billBuyReceipt = mapper.readValue(jsonString, BillBuyReceipt.class);
        return billBuyReceipt;
    }
}
