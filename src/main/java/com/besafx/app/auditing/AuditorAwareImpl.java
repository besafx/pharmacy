package com.besafx.app.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;

class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public String getCurrentAuditor() {
        PersonAwareUserDetails userDetails = (PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getPerson().getName();
    }
}