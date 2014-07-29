package com.app.dir.persistence.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Subscription {

	@Id
	@Column(name="ACCOUNT_IDENTIFIER")
	private String id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;


	@Column(name = "EDITION_CODE")
	private String editionCode;

	@Column(name = "COMPANY_UUID")
	private String companyUUID;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "MAX_USERS")
	private int maxUsers;
	
	@OneToMany(mappedBy="subscription")
	private Set<User> users;
	

    public Set<User> getUsers() { return users; }
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getEditionCode() {
		return editionCode;
	}

	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}

	public String getCompanyUUID() {
		return companyUUID;
	}

	public void setCompanyUUID(String companyUUID) {
		this.companyUUID = companyUUID;
	}

	
	@Override
	public String toString() {
		return "id: " + id + ", first name: " + firstName + ", last name: " + lastName + 
				", companyUUID: " + companyUUID + ", edition code: " + editionCode;
	}

}
