package com.app.dir.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

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
import com.app.dir.service.oauth.OAuthService;

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
			@RequestHeader(value = "Authorization") String authorizationHeader) {
		log.debug("Order Subscription Endpoint");
		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("sub_order_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new OrderSubscriptionEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public @ResponseBody EventResult changeSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Change Subscription Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		Properties prop = new Properties();

		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("sub_change_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new ChangeSubscriptionEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public @ResponseBody EventResult cancelSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Cancel Subscription Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		Properties prop = new Properties();

		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("sub_cancel_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new CancelSubscriptionEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody EventResult statusSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		Properties prop = new Properties();

		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("sub_cancel_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new StatusSubscriptionEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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

	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public @ResponseBody EventResult assignUser(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		Properties prop = new Properties();

		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("user_assign_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new UserAssignmentEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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

	@RequestMapping(value = "/unassign", method = RequestMethod.GET)
	public @ResponseBody EventResult unassignUser(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);
		Properties prop = new Properties();

		try {
			prop.load(getClass().getResourceAsStream("/consumer.properties"));
			OAuthService oAuthService = new OAuthService();
			if (oAuthService.verifySignature(authorizationHeader,
					prop.getProperty("user_unassign_url"), token)) {
				Event event;
				try {
					log.debug("beginning to parse xml");
					event = eventHandler.getEvent(token);
					log.debug("beginning to process order");
					EventProcessor ev = new UserUnassignmentEventProcessor();
					EventResult eventResult = eventHandler.processEvent(event,
							subscriptionDAO, ev);

					return eventResult;
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
