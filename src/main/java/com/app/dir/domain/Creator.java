package com.app.dir.domain;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author toantran
 * 
 * 
 *         Class in charge of Creator marshalling and unmarshalling
 *
 */
@XmlType(propOrder = { "email", "firstName", "language", "lastName", "openId",
		"uuid" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Creator {
	@XmlElement
	private String email;

	@XmlElement
	private String firstName;

	@XmlElement
	private String language;

	@XmlElement
	private String lastName;

	@XmlElement
	private String openId;

	@XmlElement
	private UUID uuid;


	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Creator)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Creator creator =  (Creator) obj;
			if( !(this.email.equals(creator.email))){
				return false;
			}
			if( !(this.firstName.equals(creator.firstName))){
				return false;
			}
			if( !(this.language.equals(creator.language))){
				return false;
			}
			if( !(this.lastName.equals(creator.lastName))){
				return false;
			}
			if( !(this.openId.equals(creator.openId))){
				return false;
			}
			if( !(this.uuid.equals(creator.uuid))){
				return false;
			}
			return true;			
		}

	}

}
