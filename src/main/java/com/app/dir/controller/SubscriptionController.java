package com.app.dir.controller;

import java.io.IOException;

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
import com.app.dir.service.oauth.HmacSha1OAuthService;
import com.app.dir.service.oauth.OAuthService;

/**
 * @author toantran
 *
 *         Controll class that handles all the REST endpoint calls from clients
 */
@Controller
@RequestMapping("/")
public class SubscriptionController {

	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionController.class);

	@Autowired
	private SubscriptionDao subscriptionDAO;

	/**
	 * Create endpoint that handles subscription ordering
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public @ResponseBody EventResult orderSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {
		log.debug("Order Subscription Endpoint");

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			OAuthService oAuthService = new HmacSha1OAuthService();
			EventProcessor eventProcessor = new OrderSubscriptionEventProcessor();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"SUBSCRIPTION_ORDER", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}

	}

	/**
	 * Change endpoint that handle subscription changes
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public @ResponseBody EventResult changeSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Change Subscription Endpoint");
		log.debug("TOKEN PASSED IS: " + token);

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			OAuthService oAuthService = new HmacSha1OAuthService();
			EventProcessor eventProcessor = new ChangeSubscriptionEventProcessor();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"SUBSCRIPTION_CHANGE", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}

	}

	/**
	 * Cancel endpoint that handle subscription cancellations
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public @ResponseBody EventResult cancelSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Cancel Subscription Endpoint");
		log.debug("TOKEN PASSED IS: " + token);

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			OAuthService oAuthService = new HmacSha1OAuthService();
			EventProcessor eventProcessor = new CancelSubscriptionEventProcessor();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"SUBSCRIPTION_CANCEL", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	/**
	 * Status endpoint that handle subscription notices
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody EventResult statusSubscription(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			EventProcessor eventProcessor = new StatusSubscriptionEventProcessor();
			OAuthService oAuthService = new HmacSha1OAuthService();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"SUBSCRIPTION_NOTICE", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	/**
	 * User assignment endpoint that handle user assignments
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public @ResponseBody EventResult assignUser(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			OAuthService oAuthService = new HmacSha1OAuthService();
			EventProcessor eventProcessor = new UserAssignmentEventProcessor();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"USER_ASSIGNMENT", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	/**
	 * User unassignment endpoint that handle unassigning users
	 * 
	 * @param token
	 *            URL token that contains the Event xml for the order
	 * @param authorizationHeader
	 *            Authorization Header that contains OAuth 1.0 signature
	 *            necessary to verify GET call was from an authorized caller
	 * @return EventResult xml
	 */
	@RequestMapping(value = "/unassign", method = RequestMethod.GET)
	public @ResponseBody EventResult unassignUser(
			@RequestParam(value = "url", required = true) String token,
			@RequestHeader(value = "Authorization") String authorizationHeader) {

		log.debug("Status Endpoint");
		log.debug("TOKEN PASSED IS: " + token);

		EventHandler eventHandler;
		try {
			eventHandler = new EventHandler();
			OAuthService oAuthService = new HmacSha1OAuthService();
			EventProcessor eventProcessor = new UserUnassignmentEventProcessor();
			return eventHandler.processEvent(eventProcessor,
					authorizationHeader, token, subscriptionDAO,
					"USER_UNASSIGNMENT", oAuthService);
		} catch (IOException e) {
			log.error("Error loading properties file", e);
			EventResult eventResult = new EventResult();
			eventResult.setSuccess(false);
			eventResult
					.setMessage("Error loading properties file need to execute service");
			eventResult.setErrorCode("CONFIGURATION_ERROR");
			return eventResult;
		}
	}

	/**
	 * Returns a list of all the users currently active
	 * 
	 * @param model
	 *            Model Map that contains list of all accouns
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView listAccounts(ModelMap model) {
		log.debug("Account List Endpoint");
		return new ModelAndView("subscriptions", "subscriptionDao",
				subscriptionDAO);
	}

	/**
	 * Returns a list of all the subscriptions currently active
	 * 
	 * @param model
	 *            Model Map that contains list of all accouns
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView listUsers(ModelMap model) {
		log.debug("Account List Endpoint");
		return new ModelAndView("users", "subscriptionDao", subscriptionDAO);
	}

}
