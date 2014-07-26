package com.app.dir.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dir.event.EventHandler;


@Controller
@RequestMapping("/")
public class EventController {
	final private EventHandler eventHandler = new EventHandler();
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String orderSubscription(@RequestParam(value="url", required=true) String token, ModelMap model) {
		
		//1) extract url
		//2) call get to url
		//3) call event handler with xml from url
		//4) get EventResult, post back to appdirect
		
		
		
		model.addAttribute("message", "post works: " + token);
		System.out.println(token);
		return "index";
	
	}
}
