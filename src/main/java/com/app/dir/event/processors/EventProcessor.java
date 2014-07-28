package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionAccountDao;

public interface EventProcessor {
	String getEventType();
	
	EventResult processEvent(Event event, SubscriptionAccountDao accountDao);
}
