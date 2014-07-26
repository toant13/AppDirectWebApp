package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;

public class StatusSubscriptionEventProcessor implements EventProcessor {

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_NOTICE";
	}

	@Override
	public EventResult processEvent(Event event) {
		// delete new app buyer information somewhere

		EventResult eventResult = new EventResult();

		eventResult.setSuccess(true);

		return eventResult;
	}

}
