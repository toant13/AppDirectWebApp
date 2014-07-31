package com.app.dir.event.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 * 
 * Unit Test StatusSubscriptionEventProcessor class
 *
 */
public class StatusSubscriptionEventProcessorTest {

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
		StatusSubscriptionEventProcessor processor = new StatusSubscriptionEventProcessor();
		assertEquals("Processor has incorrect event type matched",
				"SUBSCRIPTION_NOTICE", processor.getEventType());
	}

	/**
	 * Test processEvent method with successfaul Dao call
	 */
	@Test
	public void processEvent_SuccessfulDaoCall_SucessfulEventResult() {
		StatusSubscriptionEventProcessor processor = new StatusSubscriptionEventProcessor();
		Event event = new Event();
		EventResult actual = processor.processEvent(event, subscriptionDAO);

		assertTrue("Result should be true", actual.isSuccess());
	}

}
