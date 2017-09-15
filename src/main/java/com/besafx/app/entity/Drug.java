package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private List<TransactionBuy> transactionBuys = new ArrayList<>();

    public double getTransactionBuysSum() {
        return this.transactionBuys
                .stream()
                .mapToDouble(transactionBuy -> transactionBuy.getQuantity() * transactionBuy.getUnitBuyCost())
                .sum();
    }

    public double getBillBuyDiscountSum() {
        return this.transactionBuys
                .stream()
                .map(TransactionBuy::getBillBuy)
                .distinct()
                .mapToDouble(BillBuy::getDiscount)
                .sum();
    }


    @JsonCreator
    public static Drug Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Drug drug = mapper.readValue(jsonString, Drug.class);
        return drug;
    }
}
