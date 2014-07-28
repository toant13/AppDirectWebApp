package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionAccountDao;

public class StatusSubscriptionEventProcessor implements EventProcessor {

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_NOTICE";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionAccountDao accountDao) {
		// delete new app buyer information somewhere

		EventResult eventResult = new EventResult();

		eventResult.setSuccess(true);

		return eventResult;
	}

}
