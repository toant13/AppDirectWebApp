package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Company;

public class CompanyTest {

	@Test
	public void unmarshallCompanyXml_DummyCompanyXML_PojoWithXmlElements()
			throws JAXBException {
		Company expected = new Company();
		expected.setCountry("CA");
		expected.setEmail("company-email@example.com");
		expected.setName("Example Company Name");
		expected.setPhoneNumber("415-555-1212");
		expected.setUuid(UUID
				.fromString("d15bb36e-5fb5-11e0-8c3c-00262d2cda03"));
		expected.setWebsite("http://www.example.com");

		URL url = this.getClass().getResource("/dummy_company.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Company.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Company actual = (Company) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}

}
