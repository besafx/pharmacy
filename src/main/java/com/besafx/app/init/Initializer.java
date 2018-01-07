package com.besafx.app.init;

import com.besafx.app.Main;
import com.besafx.app.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Start Initializing From CommandLineRunner...");
    }
}
