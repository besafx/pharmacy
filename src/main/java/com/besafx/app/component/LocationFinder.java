package com.besafx.app.component;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;

@Component
public class LocationFinder {

    private final static Logger log = LoggerFactory.getLogger(LocationFinder.class);

    private DatabaseReader reader;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("location/GeoLite2-City.mmdb");
        reader = new DatabaseReader
                .Builder(classPathResource.getInputStream())
                .withCache(new CHMCache())
                .build();
    }

    public Country getCountry(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            return response.getCountry();
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return null;
        }
    }

    public City getCity(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            return response.getCity();
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return null;
        }
    }

    public Subdivision getMostSpecificSubdivision(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            return response.getMostSpecificSubdivision();
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return null;
        }
    }
}
