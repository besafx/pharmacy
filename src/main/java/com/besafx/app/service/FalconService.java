package com.besafx.app.service;
import com.besafx.app.entity.Falcon;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface FalconService extends PagingAndSortingRepository<Falcon, Long>, JpaSpecificationExecutor<Falcon> {
    Falcon findByCode(Long code);
    Falcon findByCodeAndIdIsNot(Long code, Long id);
    List<Falcon> findByCustomerId(Long customerId);
    List<Falcon> findByIdIn(List<Long> ids);
}
