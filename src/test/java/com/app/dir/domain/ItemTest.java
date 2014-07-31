package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class ItemTest {

	@Test
	public void unmarshallCompanyXml_DummyItemXML_PojoWithXmlElements()
			throws JAXBException {
		Item expected = new Item();
		expected.setQuantity(10);
		expected.setUnit("USER");


		URL url = this.getClass().getResource("/dummy_item.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Item actual = (Item) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}
	
	
	

}
