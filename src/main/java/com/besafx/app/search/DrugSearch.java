package com.besafx.app.search;

import com.besafx.app.entity.Drug;
import com.besafx.app.service.DrugService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class DrugSearch {

    private final Logger log = LoggerFactory.getLogger(DrugSearch.class);

    @Autowired
    private DrugService drugService;

    public List<Drug> filter(
            final Long codeFrom,
            final Long codeTo,
            final String nameArabic,
            final String nameEnglish,
            final String medicalNameArabic,
            final String medicalNameEnglish,
            final List<Long> drugCategories
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(nameArabic).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("nameArabic"), "%" + value + "%")));
        Optional.ofNullable(nameEnglish).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("nameEnglish"), "%" + value + "%")));
        Optional.ofNullable(medicalNameArabic).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("medicalNameArabic"), "%" + value + "%")));
        Optional.ofNullable(medicalNameEnglish).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("medicalNameEnglish"), "%" + value + "%")));
        Optional.ofNullable(drugCategories).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("drugCategory").get("id").in(value)));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = drugService.findAll(result);
            list.sort(Comparator.comparing(Drug::getCode).reversed());
            return list;
        } else {
            List<Drug> list = Lists.newArrayList(drugService.findAll());
            list.sort(Comparator.comparing(Drug::getCode).reversed());
            return list;
        }

    }
}
