package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Item;
import com.app.dir.domain.Order;

public class OrderTest {

	@Test
	public void unmarshallCompanyXml_DummyOrderXML_PojoWithXmlElements()
			throws JAXBException {
		Order expected = new Order();
		expected.setEditionCode("BASIC");
		expected.setPricingDuration("MONTHLY");
		
		Item item1 = new Item();
		item1.setQuantity(10);
		item1.setUnit("USER");
		Item item2 = new Item();
		item2.setQuantity(15);
		item2.setUnit("MEGABYTE");

		List<Item> items = new ArrayList<Item>();
		items.add(item1);
		items.add(item2);
		expected.setItems(items);
		

		URL url = this.getClass().getResource("/dummy_order.xml");
		String filePath = url.getPath();

		File file = new File(filePath);

		JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);

		// TODO: check xml escape characters. If necessary make fix for those
		// special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Order actual = (Order) jaxbUnmarshaller.unmarshal(file);

		assertEquals(
				"Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO",
				expected, actual);
	}

}
