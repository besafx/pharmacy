package com.besafx.app.entity;

import com.besafx.app.entity.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class BillSell implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billSellSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_SELL_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billSellSequenceGenerator")
    private Long id;

    private Integer code;

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    private Double discount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer checkCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @OneToMany(mappedBy = "billSell", fetch = FetchType.LAZY)
    private List<TransactionSell> transactionSells = new ArrayList<>();

    public Double getUnitSellCostSum() {
        try{
            return this.transactionSells
                    .stream()
                    .mapToDouble(transactionSell -> transactionSell.getQuantity() * transactionSell.getTransactionBuy().getUnitSellCost())
                    .sum();
        }catch (Exception ex){
            return null;
        }
    }

    public Double getNet() {
        try{
            Double totalCost = this.getUnitSellCostSum();
            return totalCost - ((totalCost * this.discount) / 100);
        }catch (Exception ex){
            return null;
        }
    }

    @JsonCreator
    public static BillSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillSell billSell = mapper.readValue(jsonString, BillSell.class);
        return billSell;
    }
}
