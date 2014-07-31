package com.app.dir.event;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.app.dir.domain.EventResult;
import com.app.dir.event.EventHandler;
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.persistence.domain.dao.SubscriptionDao;
import com.app.dir.service.oauth.OAuthService;

/**
 * @author toantran
 *
 *	Unit test for EventHandler
 */
public class EventHandlerTest {


	@InjectMocks
	private EventProcessor eventProcessor;
	
	@InjectMocks
	private SubscriptionDao subscriptionDAO;
	
	@InjectMocks
	private OAuthService oAuthService;
	
	/**
	 * Instantiates all the necessary mock classes for unit tests
	 */
	@Before
	public void testSetup(){
		this.eventProcessor = Mockito.mock(EventProcessor.class);
		this.subscriptionDAO = Mockito.mock(SubscriptionDao.class);
		this.oAuthService = Mockito.mock(OAuthService.class);
	}
	
	
	/**
	 * Performs test on processEvent method in EventHandler class when OAuth verification fails
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void processEvent_UnverifiedAuthHeader_ConfigurationErrorEventResult() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		EventHandler eventHandler = new EventHandler();
		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11272\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"GUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String token = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";
		String eventType = "SUBSCRIPTION_ORDER";
		
		Mockito.when(this.oAuthService.verifySignature(Matchers.any(String.class), Matchers.any(String.class), Matchers.any(String.class))).thenReturn(false);

		EventResult actual = eventHandler.processEvent(eventProcessor, authorizationHeader, token, subscriptionDAO, eventType, oAuthService);
		assertFalse("Event should've failed because of invalid OAuth Signature", actual.isSuccess());
	}
	
	/**
	 * Performs test on processEvent method in EventHandler class when Event Xml Retrieval fails
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void processEvent_EventXmlError_ConfigurationErrorEventResult() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		EventHandler eventHandler = new EventHandler();
		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11272\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"GUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String token = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";
		String eventType = "SUBSCRIPTION_ORDER";
		
		Mockito.when(this.oAuthService.verifySignature(Matchers.any(String.class), Matchers.any(String.class), Matchers.any(String.class))).thenReturn(true);

		EventResult actual = eventHandler.processEvent(eventProcessor, authorizationHeader, token, subscriptionDAO, eventType, oAuthService);
		assertFalse("Event should've failed because of a failure to get the event xml", actual.isSuccess());
	}
	
	/**
	 * Performs test on processEvent method in EventHandler class when Event Xml Retrieval fails
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void processEvent_BadSecretKeyInput_ConfigurationErrorEventResult() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		EventHandler eventHandler = new EventHandler();
		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11272\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"GUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String token = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";
		String eventType = "SUBSCRIPTION_ORDER";
		
		Mockito.when(this.oAuthService.verifySignature(Matchers.any(String.class), Matchers.any(String.class), Matchers.any(String.class))).thenThrow(new InvalidKeyException());

		EventResult actual = eventHandler.processEvent(eventProcessor, authorizationHeader, token, subscriptionDAO, eventType, oAuthService);
		assertFalse("Event should've failed because of a bad secret key", actual.isSuccess());
	}
}
