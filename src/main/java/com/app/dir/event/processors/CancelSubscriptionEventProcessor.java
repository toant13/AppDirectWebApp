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

		try {
			accountDao.remove(event);
			eventResult.setSuccess(true);
			eventResult.setMessage("Subscription canceled successfully");
			eventResult.setAccountIdentifier("new-account-identifier");
		} catch (IllegalArgumentException e) {
			eventResult.setSuccess(false);
			eventResult.setErrorCode("ACCOUNT_NOT_FOUND");
			eventResult.setMessage("Failed to cancel subscription because: " + e.getMessage());
		}

		return eventResult;
	}
}
