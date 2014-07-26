package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;

public interface EventProcessor {
	String getEventType();
	
	EventResult processEvent(Event event);
}
