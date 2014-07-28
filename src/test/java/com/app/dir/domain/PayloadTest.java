package com.app.dir.domain;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Company;
import com.app.dir.domain.Configuration;
import com.app.dir.domain.Entry;
import com.app.dir.domain.Item;
import com.app.dir.domain.Order;
import com.app.dir.domain.Payload;

public class PayloadTest {

	@Test
	public void unmarshallCompanyXml_DummyPayloadXML_PojoWithXmlElements()
			throws JAXBException {
		Payload expected = new Payload();
		
		Order order = new Order();
		order.setEditionCode("BASIC");
		order.setPricingDuration("MONTHLY");
		
		Item item1 = new Item();
		item1.setQuantity(10);
		item1.setUnit("USER");
		Item item2 = new Item();
		item2.setQuantity(15);
		item2.setUnit("MEGABYTE");

		List<Item> items = new ArrayList<Item>();
		items.add(item1);
		items.add(item2);
		order.setItems(items);
		
		Entry entry = new Entry();
		entry.setKey("domain");
		entry.setValue("mydomain");
		Configuration configuration = new Configuration();
		configuration.setEntry(entry);
		
		Company company = new Company();
		company.setCountry("CA");
		company.setEmail("company-email@example.com");
		company.setName("Example Company Name");
		company.setPhoneNumber("415-555-1212");
		company.setUuid(UUID
				.fromString("d15bb36e-5fb5-11e0-8c3c-00262d2cda03"));
		company.setWebsite("http://www.example.com");
		
		expected.setCompany(company);
		expected.setConfiguration(configuration);
		expected.setOrder(order);
		

		URL url = this.getClass().getResource("/dummy_payload.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Payload.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Payload actual = (Payload) jaxbUnmarshaller.unmarshal(file);


		
		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}
	
	
}
