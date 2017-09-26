package com.besafx.app.entity.wrapper;

import com.besafx.app.entity.Diagnosis;
import lombok.Data;

import java.util.List;

@Data
public class DiagnosisWrapper {
    private List<Diagnosis> diagnoses;
}
