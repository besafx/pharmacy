package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "[Order]")
public class Order implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Order.class);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double discount;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "falcon")
    private Falcon falcon;

    @ManyToOne
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @OrderBy(value = "drug")
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy(value = "detectionType")
    private List<OrderDetectionType> orderDetectionTypes = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderAttach> orderAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderReceipt> orderReceipts = new ArrayList<>();

    @JsonCreator
    public static Order Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonString, Order.class);
        return order;
    }

    public Double getDetectionTypeCostSum() {
        try {
            return this.orderDetectionTypes
                    .stream()
                    .map(OrderDetectionType::getDetectionType)
                    .mapToDouble(DetectionType::getCost)
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public String getNote() {
        try {
            if (this.note == null) {
                return "لا يوجد تشخيص حتي الآن";
            }
            return this.note;
        } catch (Exception ex) {
            return "";
        }
    }

    public Double getNetCost() {
        try {
            Double total = getDetectionTypeCostSum();
            return total - (total * this.discount / 100);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPaid() {
        try {
            return this.orderReceipts.stream()
                    .map(OrderReceipt::getReceipt)
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
            return this.getNetCost() - this.getPaid();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Long getTreatedCount() {
        try {
            return this.diagnoses.stream().filter(Diagnosis::isTreated).count();
        } catch (Exception ex) {
            return new Long(0);
        }
    }

    public Long getUnTreatedCount() {
        try {
            return this.diagnoses.size() - getTreatedCount();
        } catch (Exception ex) {
            return new Long(0);
        }
    }
}
