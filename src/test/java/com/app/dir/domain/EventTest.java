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
import com.app.dir.domain.Creator;
import com.app.dir.domain.Entry;
import com.app.dir.domain.Event;
import com.app.dir.domain.Item;
import com.app.dir.domain.Marketplace;
import com.app.dir.domain.Order;
import com.app.dir.domain.Payload;

public class EventTest {

	@Test
	public void unmarshallCompanyXml_SubscriptionOrderXML_PojoWithXmlElements()
			throws JAXBException {
		Event expected = new Event();

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
		company.setUuid(UUID.fromString("d15bb36e-5fb5-11e0-8c3c-00262d2cda03"));
		company.setWebsite("http://www.example.com");

		Payload payload = new Payload();
		payload.setCompany(company);
		payload.setConfiguration(configuration);
		payload.setOrder(order);

		Creator creator = new Creator();
		creator.setEmail("test-email+creator@appdirect.com");
		creator.setFirstName("DummyCreatorFirst");
		creator.setLanguage("fr");
		creator.setLastName("DummyCreatorLast");
		creator.setOpenId("https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2");
		creator.setUuid(UUID.fromString("ec5d8eda-5cec-444d-9e30-125b6e4b67e2"));

		Marketplace marketplace = new Marketplace();
		marketplace.setBaseUrl("https://acme.appdirect.com");
		marketplace.setPartner("ACME");

		expected.setCreator(creator);
		expected.setFlag("STATELESS");
		expected.setMarketplace(marketplace);
		expected.setPayload(payload);
		expected.setReturnUrl("https://www.appdirect.com/finishprocure?token=dummyOrder");
		expected.setType("SUBSCRIPTION_ORDER");

		URL url = this.getClass().getResource("/subscription_order.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Event actual = (Event) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}
	

}
