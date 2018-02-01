package com.besafx.app;

import com.besafx.app.entity.*;
import com.besafx.app.service.DiagnosisService;
import com.besafx.app.service.DrugUnitService;
import com.besafx.app.service.TransactionBuyService;
import com.besafx.app.service.TransactionSellService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTests {

    private final Logger log = LoggerFactory.getLogger(MainTests.class);

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private DrugUnitService drugUnitService;

    @Test
    public void contextLoads() throws Exception {

        DateTime startDate = new DateTime().withTimeAtStartOfDay();
        DateTime endDate = new DateTime().plusDays(1).withTimeAtStartOfDay();

        log.info(startDate.toString());
        log.info(endDate.toString());

    }

    private void fix1(){

        for (Diagnosis diagnosis : diagnosisService.findAll()) {

            DrugUnit drugUnit = createUnit(diagnosis.getDrugUnit(), diagnosis.getDrug());

            diagnosis.setDrugUnit(drugUnit);
            diagnosisService.save(diagnosis);

        }

        for (TransactionBuy transactionBuy : transactionBuyService.findAll()) {

            DrugUnit drugUnit = createUnit(transactionBuy.getDrugUnit(), transactionBuy.getDrug());

            transactionBuy.setDrugUnit(drugUnit);
            transactionBuyService.save(transactionBuy);

        }

        for (TransactionSell transactionSell : transactionSellService.findAll()) {

            DrugUnit drugUnit = createUnit(transactionSell.getDrugUnit(), transactionSell.getTransactionBuy().getDrug());

            transactionSell.setDrugUnit(drugUnit);
            transactionSellService.save(transactionSell);

        }

    }

    private DrugUnit createUnit(DrugUnit drugUnit, Drug drug) {
        DrugUnit unit = drugUnitService
                .findByNameAndFactorAndDrug(
                        drugUnit.getName(),
                        drugUnit.getFactor(),
                        drug);

        if (unit == null) {
            DrugUnit newUnit = new DrugUnit();
            DrugUnit topDrugUnit = drugUnitService.findTopByAndCodeIsNotNullOrderByCodeDesc();
            if (topDrugUnit == null) {
                newUnit.setCode(1);
            } else {
                newUnit.setCode(topDrugUnit.getCode() + 1);
            }
            newUnit.setName(drugUnit.getName());
            newUnit.setFactor(drugUnit.getFactor());
            newUnit.setReorderAmount(100);
            newUnit.setDrug(drug);
            unit = drugUnitService.save(newUnit);
        }

        return unit;
    }
}
