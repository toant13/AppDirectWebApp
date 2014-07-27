package com.app.dir.event;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger log = LoggerFactory
			.getLogger(EventHandler.class);
			
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
		log.info("Event processor is for type: " + eventProcessor.getEventType());
		return eventProcessor.processEvent(event);
	}
	
	public Event getEvent(RestTemplate template, String urlString){
		OAuthConsumer consumer = new DefaultOAuthConsumer("appdirectintegrationchallenge-11272", "WF0JZQZ1hJE8N7JN");
		
		
		
		try {
			URL url = new URL(urlString);
			
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			consumer.sign(request);
			request.connect();
			
			
			String s = (String) request.getContent();
			
			log.debug("it should get this far: " + s);
			
		} catch (IOException | OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Event event = template.getForObject(urlString, Event.class);
		return event;
	}
	


}
