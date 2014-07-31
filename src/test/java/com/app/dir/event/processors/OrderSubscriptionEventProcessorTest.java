package com.app.dir.event.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.app.dir.domain.Company;
import com.app.dir.domain.Configuration;
import com.app.dir.domain.Creator;
import com.app.dir.domain.Entry;
import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.domain.Item;
import com.app.dir.domain.Marketplace;
import com.app.dir.domain.Order;
import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.User;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class OrderSubscriptionEventProcessorTest {

	@InjectMocks
	private SubscriptionDao subscriptionDAO;

	private Event event;

	@Before
	public void setup() {
		this.subscriptionDAO = Mockito.mock(SubscriptionDao.class);
		event = new Event();

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

		event.setCreator(creator);
		event.setFlag("STATELESS");
		event.setMarketplace(marketplace);
		event.setPayload(payload);
		event.setReturnUrl("https://www.appdirect.com/finishprocure?token=dummyOrder");
		event.setType("SUBSCRIPTION_ORDER");
	}

	/**
	 * Test getEventType method
	 */
	@Test
	public void getEventType_Void_SubscriptionCancelString() {
		OrderSubscriptionEventProcessor processor = new OrderSubscriptionEventProcessor();
		assertEquals("Processor has incorrect event type matched",
				"SUBSCRIPTION_ORDER", processor.getEventType());
	}

	/**
	 * Test processEvent method with successfaul Dao call
	 */
	@Test
	public void processEvent_SuccessfulDaoCall_SucessfulEventResult() {
		OrderSubscriptionEventProcessor processor = new OrderSubscriptionEventProcessor();
		Mockito.doNothing()
				.when(subscriptionDAO)
				.createSubscription(Matchers.any(Subscription.class),
						Matchers.any(User.class));

		EventResult actual = processor.processEvent(event, subscriptionDAO);

		assertTrue("Result should be true", actual.isSuccess());
	}

	/**
	 * Test processEvent method with unsuccessful DAO call
	 */
	@Test
	public void processEvent_DaoException_UnsucessfulEventResult() {
		OrderSubscriptionEventProcessor processor = new OrderSubscriptionEventProcessor();

		Mockito.doThrow(new EntityExistsException())
				.when(subscriptionDAO)
				.createSubscription(Matchers.any(Subscription.class),
						Matchers.any(User.class));
		EventResult actual = processor.processEvent(event, subscriptionDAO);
		assertFalse("Result should be False", actual.isSuccess());
	}

}
