package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "key", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Entry {

	@XmlElement
	private String key;

	@XmlElement
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entry)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Entry entry =  (Entry) obj;
			if( !(this.key.equals(entry.key))){
				return false;
			}
			if( !(this.value.equals(entry.value))){
				return false;
			}
			return true;			
		}
	}
	
}
