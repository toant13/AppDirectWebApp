package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 *
 *	Handles processing events relating to subscriptions cancellations
 */
public class CancelSubscriptionEventProcessor implements EventProcessor {

	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#getEventType()
	 */
	@Override
	public String getEventType() {
		return "SUBSCRIPTION_CANCEL";
	}

	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#processEvent(com.app.dir.domain.Event, com.app.dir.persistence.domain.dao.SubscriptionDao)
	 */
	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		// delete new app buyer information somewhere

		EventResult eventResult = new EventResult();

		try {
			accountDao.removeSubscription(event.getPayload());
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
