package com.manu.test.dao;

import java.util.List;

import com.manu.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author emmanuel.mura
 *
 */
@Repository
public interface UserDao extends JpaRepository<User, Long>, UserDaoCustom {

    User findByLogin(String login);

    User findByEmail(String email);

    public List<User> findAllByOrderByIdAsc();
}
