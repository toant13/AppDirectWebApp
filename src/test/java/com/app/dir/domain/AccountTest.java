package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;


public class AccountTest {

	@Test
	public void unmarshallCompanyXml_DummyAccountXML_PojoWithXmlElements() throws JAXBException {
		Account expected = new Account();
		expected.setAccountIdentifier("MY_ACCOUNT");
		
		URL url = this.getClass().getResource("/dummy_account.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Account.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Account actual = (Account) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
		
	}

}
