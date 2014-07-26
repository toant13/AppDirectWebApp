package com.app.dir.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.event.EventHandler;

@Controller
@RequestMapping("/")
public class EventController {
	final private EventHandler eventHandler = new EventHandler();

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public @ResponseBody EventResult orderSubscription(
			@RequestParam(value = "url", required = true) String token,
			ModelMap model) {

		// 1) extract url
		// 2) call get to url
		// *handler error here
		// 3) call event handler with xml from url
		// 4) get EventResult, post back to appdirect

		RestTemplate template = new RestTemplate();

		Event event = eventHandler.getEvent(template, token);
		EventResult eventResult = eventHandler.processEvent(event);

		eventHandler.sendEventResults(template, event.getReturnUrl(),
				eventResult);

		model.addAttribute("message",
				"Great success: " + eventResult.getMessage());
		return eventResult;
	}

	@RequestMapping(value = "/post/{name}", method = RequestMethod.POST)
	public  @ResponseBody EventResult welcomeName(@PathVariable String name, ModelMap model) {

		EventResult eventResult = new EventResult();

		eventResult.setSuccess(true);
		eventResult.setMessage("Account creation successful");
		eventResult.setAccountIdentifier("new-account-identifier");
		
		return eventResult;

	}
}
