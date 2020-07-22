package com.manu.test.dao;

import java.util.List;

import com.manu.test.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author emmanuel.mura
 *
 */
@Repository
public interface UserDaoCustom {

    List<User> findByFirstName(String name) throws Exception;
}
