package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "creator", "flag", "marketplace", "payload",
		"returnUrl", "type" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Event {

	@XmlElement
	private Creator creator;

	@XmlElement
	private String flag;

	@XmlElement
	private Marketplace marketplace;

	@XmlElement
	private Payload payload;

	@XmlElement
	private String returnUrl;

	@XmlElement
	private String type;

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		this.creator = creator;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Marketplace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Event event = (Event) obj;
			if (!(this.creator.equals(event.creator))) {
				return false;
			}
			if (!(this.flag.equals(event.flag))) {
				return false;
			}
			if (!(this.marketplace.equals(event.marketplace))) {
				return false;
			}
			if (!(this.payload.equals(event.payload))) {
				return false;
			}
			if (!(this.returnUrl.equals(event.returnUrl))) {
				return false;
			}
			if (!(this.type.equals(event.type))) {
				return false;
			}

			return true;
		}
	}

}
