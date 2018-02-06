package com.besafx.app.entity;

import com.besafx.app.entity.listener.TransactionBuyListener;
import com.besafx.app.rest.DrugUnitRest;
import com.besafx.app.util.WrapperUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.ExceptionUtils;
import org.decimal4j.util.DoubleRounder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger log = LoggerFactory.getLogger(TransactionBuy.class);

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

    public List<WrapperUtil> findRelatedPrices() {
        try {
            List<WrapperUtil> wrapperUtils = new ArrayList<>();
            Gson gson = new Gson();
            String drugUnitListString = SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), DrugUnitRest.FILTER_TABLE), this.drug.getDrugUnits());
            List<DrugUnit> drugUnits = gson.fromJson(drugUnitListString, new TypeToken<List<DrugUnit>>() {
            }.getType());
            drugUnits.stream().forEach(unit -> {
                WrapperUtil util = new WrapperUtil();
                //Unit Name
                util.setObj1(unit);
                //Unit Buy Cost
                util.setObj2(DoubleRounder.round((this.getUnitBuyCost() / this.getDrugUnit().getFactor()) * unit.getFactor(), 3));
                //Unit Sell Cost
                util.setObj3(DoubleRounder.round((this.getUnitSellCost() / this.getDrugUnit().getFactor()) * unit.getFactor(), 3));
                if (unit.getId().equals(this.getDrugUnit().getId())) {
                    //Default Unit
                    util.setObj4(true);
                } else {
                    //Another Unit
                    util.setObj4(false);
                }
                //Calculate Stock
                Double stock = DoubleRounder.round(((this.getQuantity() * this.getDrugUnit().getFactor()) / unit.getFactor()), 3);
                util.setObj5(stock);
                //Calculate Real Stock
                util.setObj6(stock - this.getSalesQuantityByDrugUnit(unit));
                wrapperUtils.add(util);
            });
            return wrapperUtils;
        } catch (Exception ex) {
            org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex);
            return new ArrayList<>();
        }
    }

    public List<String> findRelatedPricesAsString() {
        try {
            List<String> strings = new ArrayList<>();
            this.findRelatedPrices().stream().forEach(wrapperUtil -> strings.add(wrapperUtil.getObj3() + " / " + ((DrugUnit)wrapperUtil.getObj1()).getName()));
            return  strings;
        } catch (Exception ex) {
            org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex);
            return new ArrayList<>();
        }
    }
}
