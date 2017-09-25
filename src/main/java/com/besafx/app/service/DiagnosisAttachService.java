package com.besafx.app.service;

import com.besafx.app.entity.Diagnosis;
import com.besafx.app.entity.DiagnosisAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DiagnosisAttachService extends PagingAndSortingRepository<DiagnosisAttach, Long>, JpaSpecificationExecutor<DiagnosisAttach> {
    List<DiagnosisAttach> findByDiagnosis(Diagnosis id);
    List<DiagnosisAttach> findByDiagnosisIn(List<Diagnosis> orders);
    List<DiagnosisAttach> findByDiagnosisId(Long id);
    List<DiagnosisAttach> findByDiagnosisIdIn(List<Long> orderIds);
}
