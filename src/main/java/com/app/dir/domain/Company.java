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
 *         Class in charge of Company marshalling and unmarshalling
 *
 */
@XmlType(propOrder = { "country", "email", "name", "phoneNumber", "uuid",
		"website" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Company {

	@XmlElement
	private String country;
	@XmlElement
	private String email;
	@XmlElement
	private String name;
	@XmlElement
	private String phoneNumber;
	@XmlElement
	private UUID uuid;
	@XmlElement
	private String website;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Company)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Company company = (Company) obj;
			if (!(this.email.equals(company.email))) {
				return false;
			}
			if (!(this.country.equals(company.country))) {
				return false;
			}
			if (!(this.email.equals(company.email))) {
				return false;
			}
			if (!(this.name.equals(company.name))) {
				return false;
			}
			if (!(this.phoneNumber.equals(company.phoneNumber))) {
				return false;
			}
			if (!(this.uuid.equals(company.uuid))) {
				return false;
			}
			if (!(this.website.equals(company.website))) {
				return false;
			}
			return true;
		}
	}

}
