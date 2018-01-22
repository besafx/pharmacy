package com.besafx.app.Async;

import com.besafx.app.entity.Drug;
import com.besafx.app.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionalService {

    @Autowired
    private DrugService drugService;

    @Transactional
    public List<Drug> getAllDrugs() {
        List<Drug> list = new ArrayList<>();
        drugService.findAll().forEach(drug -> {
            drug.getTransactionBuysSum();
            drug.getTransactionSellsSum();
            drug.getRealQuantitySum();
            list.add(drug);
        });
        return list;
    }
}
