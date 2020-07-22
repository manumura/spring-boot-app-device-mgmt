package com.manu.test.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manu.test.dao.CityDao;
import com.manu.test.entity.City;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author emmanuel.mura
 *
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    /*
     * (non-Javadoc)
     * 
     * @see CityService#findById(java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public City findById(Long id) {
//        return cityDao.findOne(id);
    	Optional<City> result = cityDao.findById(id);
    	return result.orElse(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see CityService#deleteById(java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        cityDao.deleteById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see CityService#findAll()
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<City> findAll() {
        return cityDao.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see CityService#findByCountryId(java.lang.Long)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<City> findByCountryId(Long countryId) {
        return cityDao.findByCountryId(countryId);
    }

    /**
     *
     * @param city
     * @return
     */
//    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public City create(City city) {
        return cityDao.save(city);
    }

}
