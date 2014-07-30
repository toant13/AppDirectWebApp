package com.app.dir.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.EventHandler;
import com.app.dir.event.processors.CancelSubscriptionEventProcessor;
import com.app.dir.event.processors.ChangeSubscriptionEventProcessor;
import com.app.dir.event.processors.EventProcessor;
import com.app.dir.event.processors.OrderSubscriptionEventProcessor;
import com.app.dir.event.processors.StatusSubscriptionEventProcessor;
import com.app.dir.event.processors.UserAssignmentEventProcessor;
import com.app.dir.event.processors.UserUnassignmentEventProcessor;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

@Controller
@RequestMapping("/")
public class SubscriptionController {
	final private EventHandler eventHandler = new EventHandler();
	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionController.class);

	@Autowired
	private SubscriptionDao subscriptionDAO;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public @ResponseBody EventResult orderSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String test,
			HttpServletRequest req) {

		log.debug("Create Subscription Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		log.debug("HEADER!!!: " + test);

		OAuthConsumer consumer = new DefaultOAuthConsumer(
				"appdirectintegrationchallenge-11272", "WF0JZQZ1hJE8N7JN");
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		String signedUrl = consumer.sign(token);

		log.debug("SIGNATURE!!!: " + signedUrl);

		Event event;
		try {
			log.debug("beginning to parse xml");
			event = eventHandler.getEvent(token);
			log.debug("beginning to process order");
			EventProcessor ev = new OrderSubscriptionEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);

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
		log.debug("TOKEN PASSED IS: " + token);

		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventProcessor ev = new ChangeSubscriptionEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);

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
		log.debug("TOKEN PASSED IS: " + token);

		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventProcessor ev = new CancelSubscriptionEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);

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

			EventProcessor ev = new StatusSubscriptionEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);

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

	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public @ResponseBody EventResult assignUser(
			@RequestParam(value = "url", required = true) String token) {

		log.debug("Assign User to subscription");
		log.debug("TOKEN PASSED IS: " + token);

		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventProcessor ev = new UserAssignmentEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);
			log.debug("BEFORE assignUser RETURN");
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

	@RequestMapping(value = "/unassign", method = RequestMethod.GET)
	public @ResponseBody EventResult unassignUser(
			@RequestParam(value = "url", required = true) String token) {

		log.debug("Unassign User From Subscription");

		Event event;
		try {
			event = eventHandler.getEvent(token);
			EventProcessor ev = new UserUnassignmentEventProcessor();
			EventResult eventResult = eventHandler.processEvent(event,
					subscriptionDAO, ev);
			log.debug("BEFORE assignUser RETURN");
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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView listAccounts(ModelMap model) {
		log.debug("Account List Endpoint");
		return new ModelAndView("subscriptions", "subscriptionDao",
				subscriptionDAO);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView listUsers(ModelMap model) {
		log.debug("Account List Endpoint");
		return new ModelAndView("users", "subscriptionDao", subscriptionDAO);
	}

}
