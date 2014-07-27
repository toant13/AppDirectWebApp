package com.app.dir.event;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.client.RestTemplate;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.processors.CancelSubscriptionEventProcessor;
import com.app.dir.event.processors.ChangeSubscriptionEventProcessor;
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.event.processors.OrderSubscriptionEventProcessor;
import com.app.dir.event.processors.StatusSubscriptionEventProcessor;

public class EventHandler {

	Collection<EventProcessor> eventProcessors;
	
	public EventHandler(){
		eventProcessors = new ArrayList<EventProcessor>();
		
		//Note: This would be done a way more dynamic using reflection or springs context files. For now, it is static
		eventProcessors.add(new OrderSubscriptionEventProcessor());
		eventProcessors.add(new ChangeSubscriptionEventProcessor());
		eventProcessors.add(new CancelSubscriptionEventProcessor());
		eventProcessors.add(new StatusSubscriptionEventProcessor());
	}

	//TODO: update this to make sure coming from correct endpoint
	public EventResult processEvent(Event event) {
		EventProcessor eventProcessor = null;
		
		
		
		for(EventProcessor ep : eventProcessors){
			if(event.getType().equals(ep.getEventType())){
				eventProcessor = ep;
				break;
			}
		}
		System.out.println("chosen even type : " + eventProcessor.getEventType());
		return eventProcessor.processEvent(event);
	}
	
	public Event getEvent(RestTemplate template, String url){
		Event event = template.getForObject(url, Event.class);
		return event;
	}
	


}
