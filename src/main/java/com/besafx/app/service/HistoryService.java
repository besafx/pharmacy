package com.besafx.app.service;

import com.besafx.app.auditing.Action;
import com.besafx.app.entity.History;
import com.sun.javafx.scene.control.skin.ListCellSkin;
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
public interface HistoryService extends PagingAndSortingRepository<History, Long>, JpaSpecificationExecutor<History> {
    List<History> findByModifiedDateBetweenAndActionIn(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate, List<Action> actions);
}

