package com.manu.test.service;

import java.util.List;

import com.manu.test.entity.City;

/**
 * @author emmanuel.mura
 *
 */
public interface CityService {

    City findById(Long id);

    void deleteById(Long id);

    List<City> findAll();

    List<City> findByCountryId(Long countryId);

    City create(City city);
}
