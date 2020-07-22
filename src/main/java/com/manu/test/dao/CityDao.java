package com.manu.test.dao;

import java.util.List;

import com.manu.test.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author emmanuel.mura
 *
 */
@Repository
public interface CityDao extends JpaRepository<City, Long> {

    City findByName(String name);

    List<City> findByCountryId(Long countryId);
}
