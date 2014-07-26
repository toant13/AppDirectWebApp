package com.app.dir.domain;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

public class EventResultTest {

	@Test
	public void test() throws JAXBException {
		EventResult eventResult = new EventResult();
		
		eventResult.setSuccess(true);
		eventResult.setMessage("Account creation successful");
		eventResult.setAccountIdentifier("new-account-identifier");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(EventResult.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
		jaxbMarshaller.marshal(eventResult, System.out);
 
		
	}

}
