package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionAccountDao;

public class ChangeSubscriptionEventProcessor implements EventProcessor {

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_CHANGE";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionAccountDao accountDao) {
		// store new app buyer information somewhere

		EventResult eventResult = new EventResult();
		
		
		try{
			accountDao.changeAccount(event);
			eventResult.setSuccess(true);
			eventResult.setMessage("Edition code successfully changed to: " + event.getPayload().getOrder().getEditionCode());
		}catch(IllegalArgumentException e){
			eventResult.setSuccess(false);
			eventResult.setMessage("Account changed unsuccessfully. Account does not exist.");
		}
		
		
		return eventResult;
	}

}
