package com.manu.test.controller;

import java.util.List;

import com.manu.test.entity.Country;
import com.manu.test.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author emmanuel.mura
 *
 */
@CrossOrigin(origins = "http://localhost:8091")
@RestController
@RequestMapping("/country")
public class CountryRestController {

    public static final Logger logger = LoggerFactory.getLogger(CountryRestController.class);

    @Autowired
    private CountryService countryService;

    @GetMapping()
    public ResponseEntity<Object> listAllCountries() {
        logger.info("Fetching all countries");
        List<Country> countries = countryService.findAll();
//        if (countries.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        logger.info("countries : {}", countries);

        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

}
