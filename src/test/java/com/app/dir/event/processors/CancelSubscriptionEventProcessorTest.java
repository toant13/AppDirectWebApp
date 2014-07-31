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
import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 * 
 * Unit test CancelSubscriptionEventProcessor
 *
 */
public class CancelSubscriptionEventProcessorTest {

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
		CancelSubscriptionEventProcessor processor = new CancelSubscriptionEventProcessor();
		assertEquals("Processor has incorrect event type matched",
				"SUBSCRIPTION_CANCEL", processor.getEventType());
	}

	/**
	 * Test processEvent method with successfaul Dao call
	 */
	@Test
	public void processEvent_SuccessfulDaoCall_SucessfulEventResult() {
		CancelSubscriptionEventProcessor processor = new CancelSubscriptionEventProcessor();
		Mockito.doNothing().when(subscriptionDAO)
				.removeSubscription(Matchers.any(Payload.class));

		Event event = new Event();

		EventResult actual = processor.processEvent(event, subscriptionDAO);

		assertTrue("Result should be true", actual.isSuccess());
	}

	/**
	 * Test processEvent method with unsuccessful Dao call
	 */
	@Test
	public void processEvent_DaoException_UnsuccessfulEventResult() {
		CancelSubscriptionEventProcessor processor = new CancelSubscriptionEventProcessor();

		Mockito.doThrow(new IllegalArgumentException()).when(subscriptionDAO)
				.removeSubscription(Matchers.any(Payload.class));
		Event event = new Event();
		EventResult actual = processor.processEvent(event, subscriptionDAO);
		assertFalse("Result should be False", actual.isSuccess());
	}
}
