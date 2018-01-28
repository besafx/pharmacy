package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.entity.DrugUnit;
import com.besafx.app.service.*;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.util.WrapperUtil;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/drugUnit/")
public class DrugUnitRest {

    private final static Logger log = LoggerFactory.getLogger(DrugUnitRest.class);

    public static final String FILTER_TABLE = "**,drug[id],drugUnit[id]";

    @Autowired
    private DrugUnitService drugUnitService;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_UNIT_CREATE')")
    public String create(@RequestBody DrugUnit drugUnit, Principal principal) {
        DrugUnit topDrugUnit = drugUnitService.findTopByAndCodeIsNotNullOrderByCodeDesc();
        if (topDrugUnit == null) {
            drugUnit.setCode(1);
        } else {
            drugUnit.setCode(topDrugUnit.getCode() + 1);
        }
        drugUnit = drugUnitService.save(drugUnit);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "وحدات القياس" : "Mesure Units")
                .message(lang.equals("AR") ? "تم انشاء الوحدة بنجاح" : "Create Unit Successfully")
                .type("success")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnit);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_UNIT_UPDATE')")
    public String update(@RequestBody DrugUnit drugUnit, Principal principal) {
        if (drugUnitService.findByCodeAndIdIsNot(drugUnit.getCode(), drugUnit.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        DrugUnit object = drugUnitService.findOne(drugUnit.getId());
        if (object != null) {
            drugUnit = drugUnitService.save(drugUnit);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "وحدات القياس" : "Mesure Units")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الوحدة بنجاح" : "Update Unit Information Successfully")
                    .type("warning")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnit);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_UNIT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        DrugUnit drugUnit = drugUnitService.findOne(id);
        if (drugUnit != null) {

            log.info("Delete All Related Diagnosis");
            diagnosisService.delete(diagnosisService.findByDrugUnit(drugUnit));

            log.info("Delete All Related TransactionBuy");
            transactionBuyService.delete(transactionBuyService.findByDrugUnit(drugUnit));

            log.info("Delete All Related TransactionSell");
            transactionSellService.delete(transactionSellService.findByDrugUnit(drugUnit));

            log.info("Delete DrugUnit Finally");
            drugUnitService.delete(drugUnit);

            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "وحدات القياس" : "Mesure Units")
                    .message(lang.equals("AR") ? "تم حذف الوحدة وكل ما يتعلق بها من حسابات بنجاح" : "Delete Unit With All Related Successfully")
                    .type("error")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DrugUnit> list = Lists.newArrayList(drugUnitService.findAll());
        list.sort(Comparator.comparing(DrugUnit::getFactor));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnitService.findOne(id));
    }

    @RequestMapping(value = "findByDrug/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDrug(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnitService.findByDrugId(id));
    }

    @RequestMapping(value = "getRelated/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRelated(@PathVariable(value = "id") Long id) {
//        List<DrugUnit> drugUnits = new ArrayList<>();
        DrugUnit drugUnit = drugUnitService.findOne(id);
//        if (drugUnit.getDrugUnit() == null) {
//            log.info("هذا النوع يُعتمد عليه");
//            log.info("قراءة كل الأنواع التى تعتمد عليه");
//            drugUnits = drugUnitService.findByDrugUnitId(id);
//            drugUnits.add(drugUnit);
//        } else {
//            log.info("هذا النوع لا يُعتمد عليه");
//            drugUnits.add(drugUnit.getDrugUnit());
//            drugUnits.addAll(drugUnitService.findByDrugUnitId(drugUnit.getDrugUnit().getId()));
//
//        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugUnitService.findByDrugId(drugUnit.getDrug().getId()));
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
            //Calculate Stock
            Double stock = DoubleRounder.round(((transactionBuy.getQuantity() * transactionBuy.getDrugUnit().getFactor()) / unit.getFactor()), 3);
            util.setObj5(stock);
            //Calculate Real Stock
            util.setObj6(stock - transactionBuy.getSalesQuantityByDrugUnit(unit));
            wrapperUtils.add(util);
        });
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2,obj3,obj4,obj5,obj6"), wrapperUtils);
    }

    @RequestMapping(value = "getRelatedPricesByDrug/{drugId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRelatedPricesByDrug(@PathVariable(value = "drugId") Long drugId) {
        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        Gson gson = new Gson();
        ListIterator<TransactionBuy> listIterator = transactionBuyService.findByDrugId(drugId).listIterator();
        while (listIterator.hasNext()) {
            TransactionBuy transactionBuy = listIterator.next();
            wrapperUtils.addAll(gson.fromJson(getRelatedPrices(transactionBuy.getId()), new TypeToken<List<WrapperUtil>>() {
            }.getType()));
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2,obj3,obj4,obj5"), wrapperUtils.stream().distinct().collect(Collectors.toList()));
    }
}
