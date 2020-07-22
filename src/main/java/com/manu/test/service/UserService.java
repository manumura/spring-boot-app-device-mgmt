package com.manu.test.service;

import java.util.List;

import com.manu.test.entity.User;

/**
 * @author emmanuel.mura
 *
 */
public interface UserService {

    User findById(Long id);

    User findByLogin(String login);

    User findByEmail(String email);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(Long id);

    void deleteAllUsers();

    List<User> findAllUsers();

    boolean isEmailExist(User user);

    List<User> searchByFirstName(String name) throws Exception;
}
