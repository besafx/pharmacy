package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "supplierSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUPPLIER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "supplierSequenceGenerator")
    private Long id;

    private Integer code;

    private String name;

    private String address;

    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    private String email;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<BillBuy> billBuys = new ArrayList<>();

    @JsonCreator
    public static Supplier Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Supplier supplier = mapper.readValue(jsonString, Supplier.class);
        return supplier;
    }

    public Double getNetSum() {
        try {
            return this.billBuys.stream().mapToDouble(billBuy -> billBuy.getNet()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPaidSum() {
        try {
            return this.billBuys.stream().mapToDouble(billBuy -> billBuy.getPaid()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRemainSum() {
        try {
            return this.billBuys.stream().mapToDouble(billBuy -> billBuy.getRemain()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
