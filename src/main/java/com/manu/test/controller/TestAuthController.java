package com.manu.test.controller;

import java.util.List;

import com.manu.test.entity.City;
import com.manu.test.entity.User;
import com.manu.test.service.CityService;
import com.manu.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAuthController {

    public static final Logger logger = LoggerFactory.getLogger(TestAuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/auth/users")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @PreAuthorize("#oauth2.hasScope('read')")
    public List<User> getUser() {
        return userService.findAllUsers();
    }

    @GetMapping(value = "/auth/cities")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<City> getCities() {
        return cityService.findAll();
    }
    
}