package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.service.FundService;
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
import java.util.Date;
import java.util.List;

@Data
@Entity
@Component
@EntityListeners(MyEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fund implements Serializable {

    public static final String SCREEN_NAME = "النقدية";

    private static final long serialVersionUID = 1L;

    @Transient
    private static FundService fundService;

    @PostConstruct
    public void init() {
        try {
            fundService = BeanUtil.getBean(FundService.class);
        } catch (Exception ex) {

        }
    }

    @GenericGenerator(
            name = "fundSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "FUND_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "fundSequenceGenerator")
    private Long id;

    private Long code;

    private Double tempBalance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @OneToMany(mappedBy = "fund", fetch = FetchType.LAZY)
    private List<OrderReceipt> orderReceipts = new ArrayList<>();

    @OneToMany(mappedBy = "fund", fetch = FetchType.LAZY)
    private List<BillSellReceipt> billSellReceipts = new ArrayList<>();

    @OneToMany(mappedBy = "fund", fetch = FetchType.LAZY)
    private List<FundReceipt> fundReceipts = new ArrayList<>();

    @JsonCreator
    public static Fund Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Fund fund = mapper.readValue(jsonString, Fund.class);
        return fund;
    }

    public Double getCashIn() {
        try {
            return this.fundReceipts
                    .stream()
                    .filter(fundReceipts -> fundReceipts.getReceipt().getReceiptType().equals(ReceiptType.In))
                    .mapToDouble(fundReceipt -> fundReceipt.getReceipt().getAmountNumber())
                    .sum() +
                    this.orderReceipts
                            .stream()
                            .filter(orderReceipt -> orderReceipt.getReceipt().getReceiptType().equals(ReceiptType.In))
                            .mapToDouble(orderReceipt -> orderReceipt.getReceipt().getAmountNumber())
                            .sum() +
                    this.billSellReceipts
                            .stream()
                            .filter(billSellReceipt -> billSellReceipt.getReceipt().getReceiptType().equals(ReceiptType.In))
                            .mapToDouble(billSellReceipt -> billSellReceipt.getReceipt().getAmountNumber())
                            .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getCashOut() {
        try {
            return this.fundReceipts
                    .stream()
                    .filter(fundReceipts -> fundReceipts.getReceipt().getReceiptType().equals(ReceiptType.Out))
                    .mapToDouble(fundReceipt -> fundReceipt.getReceipt().getAmountNumber())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getBalance() {
        try {
            return this.getCashIn() - this.getCashOut();
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
