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
    Falcon findByCode(Integer code);
    Falcon findByCodeAndIdIsNot(Integer code, Long id);
    List<Falcon> findByCustomerId(Long customerId);
}
