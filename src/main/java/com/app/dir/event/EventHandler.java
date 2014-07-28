package com.app.dir.event;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.processors.CancelSubscriptionEventProcessor;
import com.app.dir.event.processors.ChangeSubscriptionEventProcessor;
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.event.processors.OrderSubscriptionEventProcessor;
import com.app.dir.event.processors.StatusSubscriptionEventProcessor;
import com.app.dir.persistence.domain.dao.SubscriptionAccountDao;

public class EventHandler {
	private Properties prop = new Properties();
	Collection<EventProcessor> eventProcessors;
	private static final Logger log = LoggerFactory
			.getLogger(EventHandler.class);

	public EventHandler() {
		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
		} catch (IOException e) {
			log.error("Errorloading consumer.properties", e);
		}
		
		eventProcessors = new ArrayList<EventProcessor>();

		// Note: This would be done a way more dynamic using reflection or
		// springs context files. For now, it is static
		eventProcessors.add(new OrderSubscriptionEventProcessor());
		eventProcessors.add(new ChangeSubscriptionEventProcessor());
		eventProcessors.add(new CancelSubscriptionEventProcessor());
		eventProcessors.add(new StatusSubscriptionEventProcessor());
	}

	// TODO: update this to make sure coming from correct endpoint
	public EventResult processEvent(Event event, SubscriptionAccountDao dao) {
		EventProcessor eventProcessor = null;

		for (EventProcessor ep : eventProcessors) {
			if (event.getType().equals(ep.getEventType())) {
				eventProcessor = ep;
				break;
			}
		}
		log.info("Event processor is for type: "
				+ eventProcessor.getEventType());
		return eventProcessor.processEvent(event, dao);
	}

	// TODO: clean up exceptions, add exceptions
	public Event getEvent(String urlString) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JAXBException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				prop.getProperty("consumer-key"), prop.getProperty("consumer-secret"));

		URL url = new URL(urlString);

		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);
		request.connect();

		JAXBContext jc = JAXBContext.newInstance(Event.class);
		InputStream xml = request.getInputStream();
		Event event = (Event) jc.createUnmarshaller().unmarshal(xml);

		log.debug("AUTHORIZED TO GET XML: " + event.getCreator().getFirstName());
		return event;

	}

}
