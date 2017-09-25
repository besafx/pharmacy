package com.besafx.app.entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class DiagnosisAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "diagnosisAttachSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "DIAGNOSIS_ATTACH_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "diagnosisAttachSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attach")
    private Attach attach;

    @ManyToOne
    @JoinColumn(name = "diagnosis")
    private Diagnosis diagnosis;

    @JsonCreator
    public static DiagnosisAttach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DiagnosisAttach diagnosisAttachment = mapper.readValue(jsonString, DiagnosisAttach.class);
        return diagnosisAttachment;
    }
}
