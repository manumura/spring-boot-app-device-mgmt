package com.manu.test.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manu.test.dao.UserDao;
import com.manu.test.entity.User;

/**
 * @author emmanuel.mura
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Long id) {
//        return userDao.findOne(id);
    	Optional<User> result = userDao.findById(id);
    	return result.orElse(null);
    }

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Override
    public void updateUser(User user) {
        saveUser(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAll();
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllByOrderByIdAsc(); // findAll
    }

    @Override
    public boolean isEmailExist(User user) {
        return findByEmail(user.getEmail()) != null;
    }

    @Override
    public List<User> searchByFirstName(String name) throws Exception {
        return userDao.findByFirstName(name);
    }
}
