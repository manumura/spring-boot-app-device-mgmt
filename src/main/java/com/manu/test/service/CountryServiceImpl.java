package com.manu.test.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manu.test.dao.CountryDao;
import com.manu.test.entity.Country;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author emmanuel.mura
 *
 */
@Service("countryService")
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDao countryDao;

    /*
     * (non-Javadoc)
     * 
     * @see CountryService#findById(java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public Country findById(Long id) {
//        return countryDao.findOne(id);
    	Optional<Country> result = countryDao.findById(id);
    	return result.orElse(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see CountryService#deleteById(java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        countryDao.deleteById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see CountryService#findAll()
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<Country> findAll() {
        return countryDao.findAll();
    }

    /**
     *
     * @param country
     * @return
     */
//    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Country create(Country country) {
        return countryDao.save(country);
    }

}
