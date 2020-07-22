package com.manu.test.service;

import java.util.List;

import com.manu.test.entity.Country;

/**
 * @author emmanuel.mura
 *
 */
public interface CountryService {

    Country findById(Long id);

    void deleteById(Long id);

    List<Country> findAll();

    Country create(Country country);
}
