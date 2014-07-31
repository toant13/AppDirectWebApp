package com.app.dir.event;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
import com.app.dir.service.oauth.OAuthService;

/**
 * @author toantran
 * 
 * Handles the Event xml given from the endpoint to produce given EventResult xml output
 *
 */
public class EventHandler {
	private Properties prop = new Properties();

	private static final Logger log = LoggerFactory
			.getLogger(EventHandler.class);

	/**
	 * Constructor that loads properties file
	 * @throws IOException 
	 */
	public EventHandler() throws IOException {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
	}

	/**
	 * Makes necessary calls to classes to handle Event
	 * @param eventProcessor EventProcessor used to process event
	 * @param authorizationHeader Header containing OAuth parameters
	 * @param token used to get xml
	 * @param subscriptionDAO Data Access object used to persist subscriptions and users
	 * @param eventType Type of event to execute
	 * @param oAuthService OAuth service used to verify signature
	 * @return Result of even process
	 */
	public EventResult processEvent(EventProcessor eventProcessor,
			String authorizationHeader, String token,
			SubscriptionDao subscriptionDAO, String eventType,
			OAuthService oAuthService) {
		try {
			String urlString = prop.getProperty("ROOT_URL")
					+ prop.getProperty(eventType);

			if (oAuthService.verifySignature(authorizationHeader, urlString,
					token)) {
				Event event;
				try {
					log.debug("beginning to unmarshall xml");
					event = getEvent(token);

					if (event.getType().equals(eventType)) {
						log.debug("beginning to process order");
						EventResult eventResult = eventProcessor.processEvent(
								event, subscriptionDAO);
						return eventResult;
					} else { // incorrect event type in xml for the endpoint
								// used
						EventResult eventResult = new EventResult();
						eventResult.setSuccess(false);
						eventResult.setMessage("Error processing event");
						eventResult.setErrorCode("CONFIGURATION_ERROR");
						return eventResult;
					}
				} catch (OAuthMessageSignerException
						| OAuthExpectationFailedException
						| OAuthCommunicationException | IOException
						| JAXBException e) {

					log.error("Error processing event", e);
					EventResult eventResult = new EventResult();
					eventResult.setSuccess(false);
					eventResult.setMessage("Error processing event");
					eventResult.setErrorCode("CONFIGURATION_ERROR");
					return eventResult;
				}
			} else {
				EventResult eventResult = new EventResult();
				eventResult.setSuccess(false);
				eventResult.setMessage("Caller not authorized to use service");
				eventResult.setErrorCode("CONFIGURATION_ERROR");
				return eventResult;
			}

		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException e1) {
			log.error("Error processing event", e1);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult.setMessage("Error loading keys");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}

	}


	/**
	 * Signs token with HMA-SHA1 to enable authorization to access Event xml from the token url
	 * @param urlString Url to fetch xml
	 * @return Event object from xml fetched
	 * @throws IOException Error occurred from retrieved stream
	 * @throws OAuthMessageSignerException Failed to sign url
	 * @throws OAuthExpectationFailedException OAuth Expection failed
	 * @throws OAuthCommunicationException Connection error
	 * @throws JAXBException Xml unmarshalling error
	 */
	private Event getEvent(String urlString) throws IOException,
			OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException, JAXBException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				prop.getProperty("CONSUMER_KEY"),
				prop.getProperty("CONSUMER_SECRET"));

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
