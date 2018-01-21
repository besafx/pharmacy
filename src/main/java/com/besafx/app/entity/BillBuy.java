package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.regexp.RE;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @OneToMany(mappedBy = "billBuy", fetch = FetchType.LAZY)
    private List<TransactionBuy> transactionBuys = new ArrayList<>();

    @OneToMany(mappedBy = "billBuy", fetch = FetchType.LAZY)
    private List<BillBuyReceipt> billBuyReceipts = new ArrayList<>();

    @JsonCreator
    public static BillBuy Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillBuy billBuy = mapper.readValue(jsonString, BillBuy.class);
        return billBuy;
    }

    public List<TransactionSell> findTransactionSells() {
        try {
            return this.transactionBuys
                    .stream()
                    .flatMap(transactionBuy -> transactionBuy.getTransactionSells().stream())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Receipt> findReceipts() {
        try {
            return this.billBuyReceipts
                    .stream()
                    .map(billBuyReceipt -> billBuyReceipt.getReceipt())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getUnitBuyCostSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitBuyCost())
                    .sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getUnitSellCostSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitSellCost())
                    .sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getNet() {
        try {
            Double totalCost = this.getUnitBuyCostSum();
            return totalCost - ((totalCost * this.discount) / 100);
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getPaid() {
        try {
            return this.billBuyReceipts.stream().mapToDouble(billBuyReceipt -> billBuyReceipt.getReceipt().getAmountNumber()).sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getRemain() {
        try {
            return this.getNet() - this.getPaid();
        } catch (Exception ex) {
            return null;
        }
    }
}
