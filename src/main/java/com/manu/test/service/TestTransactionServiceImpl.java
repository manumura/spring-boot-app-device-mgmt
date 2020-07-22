package com.manu.test.service;

import com.manu.test.entity.City;
import com.manu.test.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransactionServiceImpl implements TestTransactionService {

    public static final Logger logger = LoggerFactory.getLogger(TestTransactionServiceImpl.class);

    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createCityAndCountry() {

        final Country country = new Country();
        country.setName("testcountry");
        country.setActive(true);
        final Country countryCreated = countryService.create(country);
        logger.debug("countryCreated: {}", countryCreated);

        final City city = new City();
//        city.setName("test");
        city.setCountry(country);
        city.setActive(true);
        final City cityCreated = cityService.create(city);
        logger.debug("cityCreated: {}", cityCreated);
    }
}
