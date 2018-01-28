package com.besafx.app.entity;

import com.besafx.app.auditing.MyEntityListener;
import com.besafx.app.entity.listener.DrugListener;
import com.besafx.app.entity.listener.DrugUnitListener;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.decimal4j.util.DoubleRounder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@EntityListeners(DrugUnitListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnit implements Serializable {

    public static final String SCREEN_NAME = "وحدات القياس";

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "drugUnitSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DRUG_UNIT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "drugUnitSequenceGenerator")
    private Long id;

    private Integer code;

    private String name;

    private Integer factor;

    private Integer reorderAmount;

    @JoinColumn(name = "drug")
    @ManyToOne
    private Drug drug;

    //Will be removed soon
    @JoinColumn(name = "drugUnit")
    @ManyToOne
    private DrugUnit drugUnit;

    @JsonCreator
    public static DrugUnit Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DrugUnit drugUnit = mapper.readValue(jsonString, DrugUnit.class);
        return drugUnit;
    }

    public Double getRealQuantitySum() {
        try {
            return DoubleRounder.round((this.drug.getRealQuantitySumByDrugUnit(this) / factor), 3);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRealQuantitySumByDrugUnit(DrugUnit drugUnit) {
        try {
            return DoubleRounder.round((this.drug.getRealQuantitySumByDrugUnit(drugUnit) / factor), 3);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
