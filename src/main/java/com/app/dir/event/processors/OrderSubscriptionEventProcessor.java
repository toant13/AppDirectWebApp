package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;


public class OrderSubscriptionEventProcessor implements EventProcessor{

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_ORDER";
	}

	@Override
	public EventResult processEvent(Event event) {
		//store new app buyer information somewhere
		
		EventResult eventResult = new EventResult();
		
		eventResult.setSuccess(true);
		eventResult.setMessage("Account creation successful");
		eventResult.setAccountIdentifier("new-account-identifier");
		
		
		return eventResult;
	}
	
	


	
}
