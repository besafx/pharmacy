package com.besafx.app.entity;

import com.besafx.app.auditing.Action;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class History implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(History.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "historySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "HISTORY_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "historySequenceGenerator")
    private Integer id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String objectJson;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    private String className;

    private String screenName;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(EnumType.STRING)
    private Action action;

    @JsonCreator
    public static History Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        History history = mapper.readValue(jsonString, History.class);
        return history;
    }
}
