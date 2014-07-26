package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Creator;

public class CreatorTest {

	@Test
	public void unmarshallCreatorXml_DummyCreatorXML_PojoWithXmlElements() throws JAXBException {
		Creator expected = new Creator();
		expected.setEmail("test-email+creator@appdirect.com");
		expected.setFirstName("DummyCreatorFirst");
		expected.setLanguage("fr");
		expected.setLastName("DummyCreatorLast");
		expected.setOpenId("https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2");
		expected.setUuid(UUID.fromString("ec5d8eda-5cec-444d-9e30-125b6e4b67e2"));
		
		
		URL url = this.getClass().getResource("/dummy_creator.xml");
		String filePath = url.getPath();
		
		File file = new File(filePath);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Creator.class);
		
		//TODO: check xml escape characters. If necessary make fix for those special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		Creator actual = (Creator) jaxbUnmarshaller.unmarshal(file);

		
		assertEquals("Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO", expected, actual);
	}

}
