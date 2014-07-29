package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"company","configuration","account","order" ,"user"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="payload")
public class Payload {
	
	@XmlElement(name="company")
	private Company company;
	
	@XmlElement(name="configuration")
	private Configuration configuration;
	
	@XmlElement(name="account")
	private Account account;
	
	@XmlElement(name="user")
	private User user;
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}



	@XmlElement(name="order")
	private Order order;


	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}


	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Payload)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Payload payload = (Payload) obj;
			if (!(this.company.equals(payload.company))) {
				return false;
			}
			if (!(this.configuration.equals(payload.configuration))) {
				return false;
			}
			if (!(this.order.equals(payload.order))) {
				return false;
			}

			return true;
		}
	}


	
	
	
}
