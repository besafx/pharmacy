package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.entity.listener.BillSellReceiptListener;
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
@EntityListeners(BillSellReceiptListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillSellReceipt implements Serializable {

    public static final String SCREEN_NAME = "دفعات السداد لفواتير البيع";

    private static final Logger log = LoggerFactory.getLogger(BillSellReceipt.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billSellReceiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_SELL_RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billSellReceiptSequenceGenerator")
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
