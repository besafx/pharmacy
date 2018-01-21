package com.besafx.app.service;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.BillSell;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public interface BillBuyService extends PagingAndSortingRepository<BillBuy, Long>, JpaSpecificationExecutor<BillBuy> {
    BillBuy findTopByOrderByCodeDesc();

    BillBuy findByCodeAndIdIsNot(Integer code, Long id);

    List<BillBuy> findByIdIn(List<Long> ids);

    List<BillBuy> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
