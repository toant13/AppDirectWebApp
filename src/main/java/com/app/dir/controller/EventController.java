package com.app.dir.controller;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.EventHandler;

@Controller
@RequestMapping("/")
public class EventController {
	final private EventHandler eventHandler = new EventHandler();
	private static final Logger log = LoggerFactory
			.getLogger(EventController.class);

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public @ResponseBody EventResult orderSubscription(
			@RequestParam(value = "url", required = true) String token) {

		log.debug("Create Subscription Endpoint");

		// 1) extract url
		// 2) call get to url
		// *handler error here
		// 3) call event handler with xml from url
		// 4) get EventResult, post back to appdirect



		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventResult eventResult = eventHandler.processEvent(event);

			return eventResult;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JAXBException e) {
			
			log.error("Error processing event", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult.setMessage("Error processing event");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
		
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public @ResponseBody EventResult changeSubscription(
			@RequestParam(value = "url", required = true) String token) {

		log.debug("Change Subscription Endpoint");


		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventResult eventResult = eventHandler.processEvent(event);

			return eventResult;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JAXBException e) {
			
			log.error("Error processing event", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult.setMessage("Error processing event");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public @ResponseBody EventResult cancelSubscription(
			@RequestParam(value = "url", required = true) String token) {

		log.debug("Cancel Subscription Endpoint");


		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventResult eventResult = eventHandler.processEvent(event);

			return eventResult;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JAXBException e) {
			
			log.error("Error processing event", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult.setMessage("Error processing event");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody EventResult statusSubscription(
			@RequestParam(value = "url", required = true) String token) {


		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventResult eventResult = eventHandler.processEvent(event);

			return eventResult;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JAXBException e) {
			
			log.error("Error processing event", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult.setMessage("Error processing event");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

}
