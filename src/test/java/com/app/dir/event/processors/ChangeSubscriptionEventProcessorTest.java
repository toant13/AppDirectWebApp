package com.app.dir.event.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.domain.Order;
import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class ChangeSubscriptionEventProcessorTest {

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
		ChangeSubscriptionEventProcessor processor = new ChangeSubscriptionEventProcessor();
		assertEquals("Processor has incorrect event type matched",
				"SUBSCRIPTION_CHANGE", processor.getEventType());
	}

	/**
	 * Test processEvent method with successfaul Dao call
	 */
	@Test
	public void processEvent_SuccessfulDaoCall_SucessfulEventResult() {
		ChangeSubscriptionEventProcessor processor = new ChangeSubscriptionEventProcessor();
		Mockito.doNothing().when(subscriptionDAO)
				.changeEditionCode(Matchers.any(Payload.class));

		
		Order order = new Order();
		order.setEditionCode("UPGRADE");
		Payload payload = new Payload();
		payload.setOrder(order);
		Event event = new Event();
		event.setPayload(payload);

		EventResult actual = processor.processEvent(event, subscriptionDAO);

		assertTrue("Result should be true", actual.isSuccess());
	}

	/**
	 * Test processEvent method with unsuccessful DAO call
	 */
	@Test
	public void processEvent_DaoException_UnsuccessfulEventResult() {
		ChangeSubscriptionEventProcessor processor = new ChangeSubscriptionEventProcessor();

		Mockito.doThrow(new IllegalArgumentException()).when(subscriptionDAO)
				.changeEditionCode(Matchers.any(Payload.class));
		Event event = new Event();
		EventResult actual = processor.processEvent(event, subscriptionDAO);
		assertFalse("Result should be False", actual.isSuccess());
	}
}
