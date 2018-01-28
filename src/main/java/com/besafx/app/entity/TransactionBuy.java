package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.entity.listener.TransactionBuyListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@EntityListeners(TransactionBuyListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionBuy implements Serializable {

    public static final String SCREEN_NAME = "حركات الشراء";

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "transactionBuySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TRANSACTION_BUY_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "transactionBuySequenceGenerator")
    private Long id;

    private Integer code;

    private Double unitBuyCost;

    private Double unitSellCost;

    private Double quantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date productionDate;

    private Integer warrantInMonth;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JoinColumn(name = "drug")
    @ManyToOne
    private Drug drug;

    @JoinColumn(name = "billBuy")
    @ManyToOne
    private BillBuy billBuy;

    @OneToMany(mappedBy = "transactionBuy", fetch = FetchType.LAZY)
    private List<TransactionSell> transactionSells = new ArrayList<>();

    @JsonCreator
    public static TransactionBuy Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TransactionBuy transactionBuy = mapper.readValue(jsonString, TransactionBuy.class);
        return transactionBuy;
    }

    public Double getSalesQuantity() {
        try {
            return this.transactionSells
                    .stream()
                    .mapToDouble(transactionSell -> transactionSell.getUnitQuantityByDrugUnit(this.drugUnit))
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getSalesTotalCost() {
        try {
            return this.transactionSells.stream().mapToDouble(TransactionSell::getUnitSellCost).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getSalesQuantityByDrugUnit(DrugUnit drugUnit) {
        try {
            return this.transactionSells.stream().mapToDouble(transactionSell -> transactionSell.getUnitQuantityByDrugUnit(drugUnit)).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRealQuantity() {
        try {
            return this.getQuantity() - this.getSalesQuantity();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRealQuantityByDrugUnit(DrugUnit drugUnit) {
        try {
            return (this.getQuantity() * this.drugUnit.getFactor()) - this.getSalesQuantityByDrugUnit(drugUnit);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
