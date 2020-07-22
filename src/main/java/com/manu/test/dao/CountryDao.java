package com.manu.test.dao;

import com.manu.test.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author emmanuel.mura
 *
 */
@Repository
public interface CountryDao extends JpaRepository<Country, Long> {

    Country findByName(String name);
}
