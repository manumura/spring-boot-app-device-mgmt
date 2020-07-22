package com.manu.test.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author emmanuel.mura
 *
 */
@ApiModel(description = "user model") // Swagger
@Entity
@Table(name = "app_user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "password", nullable = false)
	private String password;

	@NotBlank(message = "First name cannot be blank")
	@Size(min = 2, max = 50, message = "First name size must be between 2 and 50")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotBlank(message = "Last name cannot be blank")
	@Size(min = 2, max = 50, message = "Last name size must be between 2 and 50")
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotBlank(message = "Email cannot be blank")
	@Email
	@Column(name = "email", nullable = false)
	private String email;

	// @JsonIgnore
	@Column(name = "user_is_email_confirmed", nullable = false)
	private Boolean emailConfirmed;

	@Past
	@ApiModelProperty(notes = "Birthdate should be in the past")
	// @JsonFormat(pattern = "dd/MM/yyyy")
	// @DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "birth_date")
	private LocalDate birthDate;
	// private ZonedDateTime birthDate;

	@Min(value = 100, message = "Minimum salary is 100")
	@Digits(integer = 9, fraction = 0)
	@Column(name = "salary")
	// @Transient
	private Long salary;

	@Column(name = "gender", length = 1)
	private String gender;

	@Column(name = "is_married")
	private Boolean married;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;

	public void setUser(User user) {
		this.setEmail(user.getEmail());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setBirthDate(user.getBirthDate());
		this.setSalary(user.getSalary());
		this.setGender(user.getGender());
		this.setMarried(user.getMarried());
		this.setCountry(user.getCountry());
		this.setCity(user.getCity());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the birthDate
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the salary
	 */
	public Long getSalary() {
		return salary;
	}

	/**
	 * @param salary
	 *            the salary to set
	 */
	public void setSalary(Long salary) {
		this.salary = salary;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the married
	 */
	public Boolean getMarried() {
		return married;
	}

	/**
	 * @param married
	 *            the married to set
	 */
	public void setMarried(Boolean married) {
		this.married = married;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailConfirmed
	 */
	public Boolean getEmailConfirmed() {
		return emailConfirmed;
	}

	/**
	 * @param emailConfirmed
	 *            the emailConfirmed to set
	 */
	public void setEmailConfirmed(Boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", birthDate=" + birthDate + ", salary=" + salary + ", gender=" + gender + ", married=" + married
//				+ ", country=" + country + ", city=" + city
				+ "]";
	}

}
