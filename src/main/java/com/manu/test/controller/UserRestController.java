package com.manu.test.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.manu.test.entity.User;
import com.manu.test.exception.CustomError;
import com.manu.test.exception.NotFoundException;
import com.manu.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author emmanuel.mura
 *
 */
@CrossOrigin(origins = "http://localhost:8091")
@RestController
@RequestMapping("/user")
public class UserRestController {

	public static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;

	@GetMapping()
	public ResponseEntity<Object> listAllUsers() {
		logger.info("Fetching all Users");
		List<User> users = userService.findAllUsers();
		// if (users.isEmpty()) {
		// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		// }
		logger.info("users : {}", users);

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	// getUser API version 1
	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getUser(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			String message = "User with id " + id + " not found.";

			return new ResponseEntity<>(new CustomError(Arrays.asList(message)), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// getUser API version 2 (URI versioning)
	@GetMapping(value = "/v2/{id}") // // , headers = "X-API-VERSION=2" (header versioning)
	public EntityModel<User> getUserV2(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			String message = "User with id " + id + " not found.";

			throw new NotFoundException(message);
		}

		// HATEOAS
		// "all-users" : SERVER_PATH + "/user" => listAllUsers method
		EntityModel<User> entityModel = EntityModel.of(user);
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).listAllUsers());
		entityModel.add(linkTo.withRel("list-users"));
		logger.info("resource: {}", entityModel);
		return entityModel;
	}

	@PostMapping()
	// @RequestHeader(name = "Accept-Language", required = false) Locale locale
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		logger.info("Creating User : {}", user);

		if (userService.isEmailExist(user)) {
			logger.error("Unable to create. A User with email {} already exist", user.getEmail());
			logger.info("locale : {}", LocaleContextHolder.getLocale());
			// String message = "Unable to create. A User with email " + user.getEmail() + "
			// already exists.";
			String message = messageSource.getMessage("error.message.user.email.already.exists", null,
					LocaleContextHolder.getLocale());
			return new ResponseEntity<>(new CustomError(Arrays.asList(message)), HttpStatus.CONFLICT);
		}

		userService.saveUser(user);

		// HATEOAS : output CREATED status & the created URI : /user/{user.getId()}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
				.toUri();
		return ResponseEntity.created(location).build();

		// return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable("id") long id, @Valid @RequestBody User user) {
		logger.info("Updating User with id {}", id);
		logger.info("User new values : {}", user);

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			String message = "Unable to update. User with id " + id + " not found.";
			return new ResponseEntity<>(new CustomError(Arrays.asList(message)), HttpStatus.NOT_FOUND);
		}

		currentUser.setUser(user);
		userService.updateUser(currentUser);
		logger.info("User updated : {}", currentUser);
		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			String message = "Unable to delete. User with id " + id + " not found.";
			return new ResponseEntity<>(new CustomError(Arrays.asList(message)), HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/searchByFirstName/{name}")
	public ResponseEntity<Object> searchByFirstName(@PathVariable("name") String name) {
		logger.info("Search by first name: {}", name);
		try {
			List<User> users = userService.searchByFirstName(name);
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.info("users : {}", users);

			return new ResponseEntity<>(users, HttpStatus.OK);

		} catch (Exception e) {
			return handleException(e);
		}

	}

	private ResponseEntity<Object> handleException(Exception e) {
		logger.error(e.getMessage(), e);
		String message = "An unexpected error occured";
		return new ResponseEntity<>(Arrays.asList(message), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
	}

}
