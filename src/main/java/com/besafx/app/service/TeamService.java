package com.besafx.app.service;

import com.besafx.app.entity.Team;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TeamService extends PagingAndSortingRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    Team findTopByOrderByCodeDesc();

    Team findByCodeAndIdIsNot(Integer code, Long id);

    Team findByAuthorities(String authorities);
}
