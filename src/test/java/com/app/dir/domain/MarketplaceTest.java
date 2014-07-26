package com.app.dir.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.app.dir.domain.Marketplace;

public class MarketplaceTest {


	@Test
	public void unmarshallMarketplaceXml_DummyMarketplaceXML_PojoWithXmlElements() throws JAXBException {
		Marketplace expected = new Marketplace();
		expected.setBaseUrl("https://acme.appdirect.com");
		expected.setPartner("ACME");
		
		
		URL url = this.getClass().getResource("/dummy_marketplace.xml");
		String filePath = url.getPath();
		
		File file = new File(filePath);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Marketplace.class);
		
		//TODO: check xml escape characters. If necessary make fix for those special cases.
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		Marketplace actual = (Marketplace) jaxbUnmarshaller.unmarshal(file);
		
		assertEquals("Unmarshall test failed. Input XML unsuccessfully unmarshall to POJO", expected, actual);
	}

}
