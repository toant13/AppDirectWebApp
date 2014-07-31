package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 * 
 * Handles events relating to subscription notice changes
 *
 */
public class StatusSubscriptionEventProcessor implements EventProcessor {

	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#getEventType()
	 */
	@Override
	public String getEventType() {
		return "SUBSCRIPTION_NOTICE";
	}

	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#processEvent(com.app.dir.domain.Event, com.app.dir.persistence.domain.dao.SubscriptionDao)
	 */
	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		EventResult eventResult = new EventResult();
		eventResult.setSuccess(true);
		return eventResult;
	}

}
