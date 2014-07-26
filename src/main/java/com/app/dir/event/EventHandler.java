package com.app.dir.event;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.client.RestTemplate;

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

	//TODO: update this to make sure coming from correct endpoint
	public EventResult processEvent(Event event) {
		EventProcessor eventProcessor = null;
		
		System.out.println(event.getType());
		
		for(EventProcessor ep : eventProcessors){
			if(event.getType().equals(ep.getEventType())){
				eventProcessor = ep;
				break;
			}
		}
		return eventProcessor.processEvent(event);
	}
	
	public Event getEvent(RestTemplate template, String url){
		Event event = template.getForObject(url, Event.class);
		return event;
	}
	
	public URI sendEventResults(RestTemplate template, String url, EventResult eventResult){
		URI results = template.postForLocation(url, eventResult);
		return results;
	}

}
