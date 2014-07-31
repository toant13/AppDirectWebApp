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
 *         Class in charge of Item marshalling and unmarshalling
 *
 */
@XmlType(propOrder = { "quantity", "unit" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Item {
	@XmlElement
	private int quantity;

	@XmlElement
	private String unit;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Item)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Item entry =  (Item) obj;
			if( !(this.quantity == entry.quantity)){
				return false;
			}
			if( !(this.unit.equals(entry.unit))){
				return false;
			}
			return true;			
		}
	}
	
	
}
