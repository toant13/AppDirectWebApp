package com.app.dir.event;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class EventHandler {
	private Properties prop = new Properties();

	private static final Logger log = LoggerFactory
			.getLogger(EventHandler.class);

	public EventHandler() {
		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
		} catch (IOException e) {
			log.error("Errorloading consumer.properties", e);
		}

	}

	// TODO: update this to make sure coming from correct endpoint
	public EventResult processEvent(Event event, SubscriptionDao dao, EventProcessor eventProcessor) {
		return eventProcessor.processEvent(event, dao);
	}

	// TODO: clean up exceptions, add exceptions
	public Event getEvent(String urlString) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JAXBException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				prop.getProperty("consumer-key"), prop.getProperty("consumer-secret"));

		log.debug("GETTING XML FROM APP DIRECT");
		
		URL url = new URL(urlString);

		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);
		request.connect();
		
		JAXBContext jc = JAXBContext.newInstance(Event.class);
		log.debug("CONNECTING GETTING INPUT STREAM");
		InputStream xml = request.getInputStream();
		Event event = (Event) jc.createUnmarshaller().unmarshal(xml);

		log.debug("AUTHORIZED TO GET XML: " + event.getCreator().getFirstName());
		return event;

	}
	

}
