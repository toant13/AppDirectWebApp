package com.app.dir.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author toantran
 * 
 * 
 *         Class in charge of Order marshalling and unmarshalling
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="order")
public class Order {
	
	@XmlElement
	private String editionCode;

	@XmlElement(name="item")
	private List<Item> items;
	
	
	@XmlElement
	private String pricingDuration;


	public String getEditionCode() {
		return editionCode;
	}


	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}


	public List<Item> getItems() {
		return items;
	}


	public void setItems(List<Item> items) {
		this.items = items;
	}


	public String getPricingDuration() {
		return pricingDuration;
	}


	public void setPricingDuration(String pricingDuration) {
		this.pricingDuration = pricingDuration;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Order)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Order order = (Order) obj;
			if(this.editionCode !=null && order.editionCode !=null){
				if (!(this.editionCode.equals(order.editionCode))) {
					return false;
				}
			}
			if (!(this.items.equals(order.items))) {
				return false;
			}
			if(this.pricingDuration !=null && order.pricingDuration !=null){	
				if (!(this.pricingDuration.equals(order.pricingDuration))) {
					return false;
				}
			}else
			{
				return true;
			}

			return true;
		}
	}
	
	

}
