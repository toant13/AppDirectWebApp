package com.app.dir.event;

import java.util.ArrayList;
import java.util.Collection;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.event.processors.OrderSubscriptionEventProcessor;

public class EventHandler {

	Collection<EventProcessor> eventProcessors;
	
	public EventHandler(){
		eventProcessors = new ArrayList<EventProcessor>();
		
		//Note: This would be done a way more dynamic using reflection. For now, it is static
		eventProcessors.add(new OrderSubscriptionEventProcessor());
	}

	public EventResult processEvent(Event event) {
		EventProcessor eventProcessor = null;
		
		for(EventProcessor ep : eventProcessors){
			if(event.getType().equals(ep.getEventType())){
				eventProcessor = ep;
				break;
			}
		}
		return eventProcessor.processEvent(event);
	}

}
