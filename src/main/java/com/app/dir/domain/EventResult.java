package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author toantran
 * 
 * 
 *         Class in charge of EventResult marshalling and unmarshalling
 *
 */
@XmlType(propOrder = { "success", "errorCode", "message", "accountIdentifier" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="result")
public class EventResult {
	
	@XmlElement
	private boolean success;
	
	@XmlElement
	private String errorCode;
	
	@XmlElement
	private String message;
	
	@XmlElement
	private String accountIdentifier;



	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}

	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
}
