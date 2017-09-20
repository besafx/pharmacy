package com.besafx.app.rest;

import com.besafx.app.entity.DrugUnit;
import com.besafx.app.entity.TransactionBuy;
import com.besafx.app.service.DrugUnitService;
import com.besafx.app.service.TransactionBuyService;
import com.besafx.app.util.WrapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.decimal4j.util.DoubleRounder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/drugUnit/")
public class DrugUnitRest {

    private final static Logger log = LoggerFactory.getLogger(DrugUnitRest.class);

    public static final String FILTER_TABLE = "**";

    @Autowired
    private DrugUnitService drugUnitService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DrugUnit> list = Lists.newArrayList(drugUnitService.findAll());
        list.sort(Comparator.comparing(DrugUnit::getFactor));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "getRelated/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRelated(@PathVariable(value = "id") Long id) {
        List<DrugUnit> drugUnits = new ArrayList<>();
        DrugUnit drugUnit = drugUnitService.findOne(id);
        if (drugUnit.getDrugUnit() == null) {
            log.info("هذا النوع يُعتمد عليه");
            log.info("قراءة كل الأنواع التى تعتمد عليه");
            drugUnits = drugUnitService.findByDrugUnitId(id);
            drugUnits.add(drugUnit);
        } else {
            log.info("هذا النوع لا يُعتمد عليه");
            drugUnits.add(drugUnit.getDrugUnit());
            drugUnits.addAll(drugUnitService.findByDrugUnitId(drugUnit.getDrugUnit().getId()));

        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnits);
    }

    @RequestMapping(value = "getRelatedPrices/{transactionBuyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRelatedPrices(@PathVariable(value = "transactionBuyId") Long transactionBuyId) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(transactionBuyId);
        Gson gson = new Gson();
        List<DrugUnit> drugUnits = gson.fromJson(getRelated(transactionBuy.getDrugUnit().getId()), new TypeToken<List<DrugUnit>>() {
        }.getType());
        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        drugUnits.stream().forEach(unit -> {
            WrapperUtil util = new WrapperUtil();
            //Unit Name
            util.setObj1(unit);
            //Unit Buy Cost
            util.setObj2(DoubleRounder.round((transactionBuy.getUnitBuyCost() / transactionBuy.getDrugUnit().getFactor()) * unit.getFactor(), 3));
            //Unit Sell Cost
            util.setObj3(DoubleRounder.round((transactionBuy.getUnitSellCost() / transactionBuy.getDrugUnit().getFactor()) * unit.getFactor(), 3));
            if (unit.getId().equals(transactionBuy.getDrugUnit().getId())) {
                //Default Unit
                util.setObj4(true);
            } else {
                //Another Unit
                util.setObj4(false);
            }
            //Calculate Quantity
            util.setObj5(DoubleRounder.round((transactionBuy.getDrugUnit().getFactor() / unit.getFactor()) * transactionBuy.getQuantity(), 3));
            wrapperUtils.add(util);
        });
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2,obj3,obj4,obj5"), wrapperUtils);
    }

    @RequestMapping(value = "getRelatedPricesByDrug/{drugId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRelatedPricesByDrug(@PathVariable(value = "drugId") Long drugId) {
        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        Gson gson = new Gson();
        ListIterator<TransactionBuy> listIterator = transactionBuyService.findByDrugId(drugId).listIterator();
        while(listIterator.hasNext()){
            TransactionBuy transactionBuy = listIterator.next();
            wrapperUtils.addAll(gson.fromJson(getRelatedPrices(transactionBuy.getId()), new TypeToken<List<WrapperUtil>>(){}.getType()));
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2,obj3,obj4,obj5"), wrapperUtils.stream().distinct().collect(Collectors.toList()));
    }
}
