package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author toantran
 * 
 *         Class in charge of Account marshalling and unmarshalling
 *
 */
@XmlType(propOrder = { "accountIdentifier", "status" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Account {

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}

	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	@XmlElement
	private String accountIdentifier;

	@XmlElement
	private String status;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Account)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Account entry = (Account) obj;
			if (!(this.accountIdentifier.equals(entry.accountIdentifier))) {
				return false;
			}

			return true;
		}
	}

}
