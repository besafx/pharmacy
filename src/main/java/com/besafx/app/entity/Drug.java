package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.listener.DrugListener;
import com.besafx.app.service.BankService;
import com.besafx.app.service.DrugUnitService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Component
@EntityListeners(DrugListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Drug implements Serializable {

    public static final String SCREEN_NAME = "الأدوية والاصناف";

    private static final long serialVersionUID = 1L;

    @Transient
    private static DrugUnitService drugUnitService;

    @PostConstruct
    public void init() {
        try {
            drugUnitService = BeanUtil.getBean(DrugUnitService.class);
        } catch (Exception ex) {}
    }

    @GenericGenerator(
            name = "drugSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DRUG_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "drugSequenceGenerator")
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    private String medicalNameArabic;

    private String medicalNameEnglish;

    private String descriptionArabic;

    private String descriptionEnglish;

    @ManyToOne
    @JoinColumn(name = "drugCategory")
    private DrugCategory drugCategory;

    @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
    private List<DrugUnit> drugUnits = new ArrayList<>();

    @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
    private List<TransactionBuy> transactionBuys = new ArrayList<>();

    @JsonCreator
    public static Drug Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Drug drug = mapper.readValue(jsonString, Drug.class);
        return drug;
    }

    public DrugUnit getDefaultDrugUnit() {
        try {
            return drugUnitService.findTopByDrugOrderByFactorAsc(this);
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getRealQuantitySum() {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getRealQuantity())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRealQuantitySumByDrugUnit(DrugUnit drugUnit) {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getRealQuantityByDrugUnit(drugUnit))
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTransactionBuysSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitBuyCost())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTransactionSellsSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .mapToDouble(TransactionBuy::getSalesTotalCost)
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getBillBuyDiscountSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .map(TransactionBuy::getBillBuy)
                    .distinct()
                    .mapToDouble(BillBuy::getDiscount)
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getBillSellDiscountSum() {
        try {
            return this.transactionBuys
                    .stream()
                    .flatMap(transactionBuy -> transactionBuy.getTransactionSells().stream())
                    .map(TransactionSell::getBillSell)
                    .distinct()
                    .mapToDouble(BillSell::getDiscount)
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public List<TransactionSell> getTransactionSells() {
        try {
            return this.transactionBuys
                    .stream()
                    .flatMap(transactionBuy -> transactionBuy.getTransactionSells().stream())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}
