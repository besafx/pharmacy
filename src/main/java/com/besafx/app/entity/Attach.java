package com.besafx.app.entity;

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
public class Attach implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "attachSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ATTACH_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "attachSequenceGenerator")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String name;

    private String mimeType;

    private Boolean remote;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String link;

    private Long size;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @JsonCreator
    public static Attach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Attach attach = mapper.readValue(jsonString, Attach.class);
        return attach;
    }
}
