package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;

public class ChangeSubscriptionEventProcessor implements EventProcessor {

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_CHANGE";
	}

	@Override
	public EventResult processEvent(Event event) {
		// store new app buyer information somewhere

		EventResult eventResult = new EventResult();

		eventResult.setSuccess(true);
		eventResult.setMessage("Account changed successfully");
		eventResult.setAccountIdentifier("account name");

		return eventResult;
	}

}
