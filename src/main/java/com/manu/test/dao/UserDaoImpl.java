package com.manu.test.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.manu.test.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author emmanuel.mura
 *
 */
public class UserDaoImpl implements UserDaoCustom {

    public static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    /*
     * (non-Javadoc)
     * 
     * @see UserDaoCustom#findByFirstName(java.lang.String)
     */
    @Override
    public List<User> findByFirstName(String name) throws Exception {

        Assert.notNull(name, "Name must not be null!");

        String sql = "from User u where upper(u.firstName) like CONCAT(:firstName, '%')";
        TypedQuery<User> query = em.createQuery(sql, User.class);
        query.setParameter("firstName", name.toUpperCase());
        List<User> results = query.getResultList();
        logger.debug("Users found : {}", results);
        return results;
    }

}
