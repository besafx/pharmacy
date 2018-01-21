package com.besafx.app.entity;

import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.service.BankService;
import com.besafx.app.service.FundService;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private static BankService bankService;

    @PostConstruct
    public void init() {
        try {
            bankService = BeanUtil.getBean(BankService.class);
        } catch (Exception ex) {

        }
    }

    @GenericGenerator(
            name = "bankSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BANK_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "bankSequenceGenerator")
    private Long id;

    private Long code;

    private Double tempBalance;

    private String name;

    private String branchName;

    private Double startAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startAmountDate;

    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
    private List<BankReceipt> bankReceipts = new ArrayList<>();

    @JsonCreator
    public static Bank Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Bank bank = mapper.readValue(jsonString, Bank.class);
        return bank;
    }

    public Double getCashIn() {
        try {
            return this.bankReceipts
                    .stream()
                    .filter(bankReceipts -> bankReceipts.getReceipt().getReceiptType().equals(ReceiptType.In))
                    .mapToDouble(bankReceipt -> bankReceipt.getReceipt().getAmountNumber())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getCashOut() {
        try {
            return this.bankReceipts
                    .stream()
                    .filter(bankReceipts -> bankReceipts.getReceipt().getReceiptType().equals(ReceiptType.Out))
                    .mapToDouble(bankReceipt -> bankReceipt.getReceipt().getAmountNumber())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getBalance() {
        try {
            this.tempBalance = this.getCashIn() - this.getCashOut();
            bankService.save(this);
            return this.tempBalance;
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
