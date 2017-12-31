package com.besafx.app.entity;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "receiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "receiptSequenceGenerator")
    private Long id;

    private Long code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long paymentMethodCode;

    private String amountString;

    private Double amountNumber;

    private String sender;

    private String receiver;

    @Enumerated(EnumType.STRING)
    private ReceiptType receiptType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "lastPerson")
    private Person lastPerson;

    public String getPaymentMethodInArabic(){
        try{
            return this.paymentMethod.getName();
        }catch (Exception ex){
            return "";
        }
    }

    @JsonCreator
    public static Receipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Receipt receipt = mapper.readValue(jsonString, Receipt.class);
        return receipt;
    }
}
