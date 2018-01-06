package com.besafx.app.entity;

import com.besafx.app.component.BeanUtil;
import com.besafx.app.service.TransactionSellService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Component
public class Diagnosis implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Diagnosis.class);

    private static final long serialVersionUID = 1L;

    @Transient
    private static TransactionSellService transactionSellService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "[usage]")
    private String usage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double quantity;

    @JoinColumn(name = "drug")
    @ManyToOne
    @OrderBy(value = "code")
    private Drug drug;

    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JoinColumn(name = "[order]")
    @ManyToOne
    private Order order;

    @JsonCreator
    public static Diagnosis Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Diagnosis diagnosis = mapper.readValue(jsonString, Diagnosis.class);
        return diagnosis;
    }

    @PostConstruct
    public void init() {
        try {
            transactionSellService = BeanUtil.getBean(TransactionSellService.class);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    public Boolean isTreated() {
        try {
            return transactionSellService.countByBillSellOrderAndTransactionBuyDrug(this.order, this.getDrug()) > 0;
        } catch (Exception ex) {
            return false;
        }
    }
}
