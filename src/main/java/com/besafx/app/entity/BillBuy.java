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
public class BillBuy implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billBuySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_BUY_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billBuySequenceGenerator")
    private Long id;

    private Integer code;

    @JoinColumn(name = "supplier")
    @ManyToOne
    private Supplier supplier;

    private Double discount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer checkCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @OneToMany(mappedBy = "billBuy", fetch = FetchType.LAZY)
    private List<TransactionBuy> transactionBuys = new ArrayList<>();

    public Double getUnitBuyCostSum() {
        try{
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitBuyCost())
                    .sum();
        }catch (Exception ex){
            return null;
        }
    }

    public Double getUnitSellCostSum() {
        try{
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitSellCost())
                    .sum();
        }catch (Exception ex){
            return null;
        }
    }

    public Double getNet() {
        try{
            Double totalCost = this.getUnitBuyCostSum();
            return totalCost - ((totalCost * this.discount) / 100);
        }catch (Exception ex){
            return null;
        }
    }


    @JsonCreator
    public static BillBuy Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillBuy billBuy = mapper.readValue(jsonString, BillBuy.class);
        return billBuy;
    }
}
