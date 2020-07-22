package com.manu.test.controller;

import java.util.List;

import com.manu.test.entity.City;
import com.manu.test.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author emmanuel.mura
 *
 */
@CrossOrigin(origins = "http://localhost:8091")
@RestController
@RequestMapping("/city")
public class CityRestController {

    public static final Logger logger = LoggerFactory.getLogger(CityRestController.class);

    @Autowired
    private CityService cityService;

    @GetMapping()
    public ResponseEntity<Object> listAllCities() {
        logger.info("Fetching all cities");
        List<City> cities = cityService.findAll();
//        if (cities.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        logger.info("cities : {}", cities);

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(value = "/country/{id}")
    public ResponseEntity<Object> listAllCitiesForCountry(@PathVariable("id") Long countryId) {
        logger.info("Fetching all cities for country id: {}", countryId);
        List<City> cities = cityService.findByCountryId(countryId);
        if (cities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("cities : {}", cities);

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

}
