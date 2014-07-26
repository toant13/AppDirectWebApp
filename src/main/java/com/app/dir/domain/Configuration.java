package com.app.dir.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "entry" })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Configuration {
	private Entry entry;

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Configuration)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			final Configuration Configuration = (Configuration) obj;
			if (!(this.entry.equals(Configuration.entry))) {
				return false;
			}

			return true;
		}
	}

}
