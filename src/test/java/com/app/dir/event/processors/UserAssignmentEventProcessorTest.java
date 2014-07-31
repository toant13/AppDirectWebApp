package com.app.dir.event.processors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.app.dir.domain.User;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 * 
 *         Unit test UserAssignmentEventProcessor class
 */
public class UserAssignmentEventProcessorTest {

	@InjectMocks
	private SubscriptionDao subscriptionDAO;

	@Before
	public void setup() {
		this.subscriptionDAO = Mockito.mock(SubscriptionDao.class);
	}

	/**
	 * Test getEventType method
	 */
	@Test
	public void getEventType_Void_SubscriptionCancelString() {
		UserAssignmentEventProcessor processor = new UserAssignmentEventProcessor();
		assertEquals("Processor has incorrect event type matched",
				"USER_ASSIGNMENT", processor.getEventType());
	}

	/**
	 * Test processEvent method with successful Dao call
	 */
	@Test
	public void processEvent_SuccessfulDaoCall_SucessfulEventResult() {
		UserAssignmentEventProcessor processor = new UserAssignmentEventProcessor();
		Mockito.doNothing().when(subscriptionDAO)
				.assignUser(Matchers.any(Event.class));

		Event event = new Event();

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

		User user = new User();
		user.setFirstName("firstName");
		user.setLastName("lastName");
		
		Payload payload = new Payload();
		payload.setCompany(company);
		payload.setConfiguration(configuration);
		payload.setOrder(order);
		payload.setUser(user);

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

		EventResult actual = processor.processEvent(event, subscriptionDAO);

		assertTrue("Result should be true", actual.isSuccess());
	}

	/**
	 * Test processEvent method with unsuccessful Dao call
	 */
	@Test
	public void processEvent_NoSubscriptinFoundInput_UnsuccessfulEventResult() {
		UserAssignmentEventProcessor processor = new UserAssignmentEventProcessor();

		Mockito.doThrow(new IllegalArgumentException()).when(subscriptionDAO)
				.assignUser(Matchers.any(Event.class));
		Event event = new Event();
		EventResult actual = processor.processEvent(event, subscriptionDAO);
		assertEquals("Result message is incorrect", "ACCOUNT_NOT_FOUND", actual.getErrorCode());
	}
	
	/**
	 * Test processEvent method with unsuccessful Dao call
	 */
	@Test
	public void processEvent_TooManyUsers_UnsuccessfulEventResult() {
		UserAssignmentEventProcessor processor = new UserAssignmentEventProcessor();

		Mockito.doThrow(new IllegalStateException()).when(subscriptionDAO)
				.assignUser(Matchers.any(Event.class));
		Event event = new Event();
		EventResult actual = processor.processEvent(event, subscriptionDAO);
		assertEquals("Result message is incorrect", "MAX_USERS_REACHED", actual.getErrorCode());
	}

}
