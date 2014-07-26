package com.app.dir.domain;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Configuration;
import com.app.dir.domain.Entry;

public class ConfigurationTest {

	@Test
	public void unmarshallCompanyXml_DummyConfigurationXML_PojoWithXmlElements()
			throws JAXBException {
		Entry entry = new Entry();
		entry.setKey("domain");
		entry.setValue("mydomain");
		Configuration expected = new Configuration();
		expected.setEntry(entry);


		URL url = this.getClass().getResource("/dummy_configuration.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Configuration actual = (Configuration) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}

}
