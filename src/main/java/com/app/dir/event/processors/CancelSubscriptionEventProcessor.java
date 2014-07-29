package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class CancelSubscriptionEventProcessor implements EventProcessor {
	
	@Override
	public String getEventType() {
		return "SUBSCRIPTION_CANCEL";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		// delete new app buyer information somewhere

		EventResult eventResult = new EventResult();

		eventResult.setSuccess(true);
		eventResult.setMessage("Account canceled successful");
		eventResult.setAccountIdentifier("new-account-identifier");

		return eventResult;
	}
}
