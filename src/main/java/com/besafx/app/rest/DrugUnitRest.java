package com.besafx.app.rest;

import com.besafx.app.entity.DrugUnit;
import com.besafx.app.service.DrugUnitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/drugUnit/")
public class DrugUnitRest {

    private final static Logger log = LoggerFactory.getLogger(DrugUnitRest.class);

    public static final String FILTER_TABLE = "**";

    @Autowired
    private DrugUnitService drugUnitService;

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DrugUnit> list = Lists.newArrayList(drugUnitService.findAll());
        list.sort(Comparator.comparing(DrugUnit::getFactor));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
