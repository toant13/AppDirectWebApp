package com.app.dir.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author toantran
 * Entity relating to User related data
 */
@Entity
public class User {


	@Id
	@Column(name="USER_ID")
	private String UserID;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "OPEN_ID")
	private String openId;
	
	@Column(name = "EMAIL")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToOne
    @JoinColumn(name="id")
	private Subscription subscription;
	

    public Subscription getSubscription() { return subscription; }
	
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
	

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
