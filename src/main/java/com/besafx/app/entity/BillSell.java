package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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

    private String customerName;

    private String falconCode;

    //Optional Field In Case Of Inside Selling
    @JoinColumn(name = "[order]")
    @ManyToOne
    private Order order;

    private Double discount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @OneToMany(mappedBy = "billSell", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<TransactionSell> transactionSells = new ArrayList<>();

    @OneToMany(mappedBy = "billSell", fetch = FetchType.LAZY)
    private List<BillSellReceipt> billSellReceipts = new ArrayList<>();

    @JsonCreator
    public static BillSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillSell billSell = mapper.readValue(jsonString, BillSell.class);
        return billSell;
    }

    public List<Receipt> findReceipts() {
        try {
            return this.billSellReceipts
                    .stream()
                    .map(billBuyReceipt -> billBuyReceipt.getReceipt())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getUnitSellCostSum() {
        try {
            return this.transactionSells
                    .stream()
                    .mapToDouble(transactionSell -> transactionSell.getQuantity() * transactionSell.getTransactionBuy().getUnitSellCost())
                    .sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getNet() {
        try {
            Double totalCost = this.getUnitSellCostSum();
            return totalCost - ((totalCost * this.discount) / 100);
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getPaid() {
        try {
            return this.billSellReceipts.stream()
                    .map(BillSellReceipt::getReceipt)
                    .collect(Collectors.toList())
                    .stream()
                    .mapToDouble(Receipt::getAmountNumber)
                    .sum();

        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRemain() {
        try {
            return this.getNet() - this.getPaid();
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
