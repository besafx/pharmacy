package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.entity.listener.TransactionSellListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.decimal4j.util.DoubleRounder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(TransactionSellListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionSell implements Serializable {

    public static final String SCREEN_NAME = "حركات البيع";

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

    private Double quantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    private Double unitCost;

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

    public Double getUnitSellCost() {
        try {
            return DoubleRounder.round(
                    (this.transactionBuy.getUnitSellCost() / this.transactionBuy.getDrugUnit().getFactor()) * this.drugUnit.getFactor(), 3);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getUnitQuantity() {
        try {
            if (this.transactionBuy.getDrugUnit().equals(this.drugUnit)) {
                return this.quantity;
            }
            return DoubleRounder.round((this.quantity / this.drugUnit.getFactor()), 3);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getUnitQuantityByDrugUnit(DrugUnit drugUnit) {
        try {
            if (this.transactionBuy.getDrugUnit().equals(this.drugUnit)) {
                return this.quantity;
            }
            return DoubleRounder.round(((this.quantity / this.drugUnit.getFactor()) / drugUnit.getFactor()), 3);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
