package com.app.dir.event.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class UserAssignmentEventProcessor implements EventProcessor {
	private static final Logger log = LoggerFactory
			.getLogger(UserAssignmentEventProcessor.class);
	
	@Override
	public String getEventType() {
		return "USER_ASSIGNMENT";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		EventResult eventResult = new EventResult();
		log.debug("invoking processEvent method");
		
		try {
			accountDao.assignUser(event.getPayload());
			eventResult.setSuccess(true);
			eventResult.setMessage("Successfully Assigned: "
					+ event.getPayload().getUser().getFirstName() + " " + event.getPayload().getUser().getLastName());
		} catch (IllegalArgumentException e) {
			eventResult.setSuccess(false);
			eventResult.setErrorCode("ACCOUNT_NOT_FOUND");
			eventResult.setMessage("Failed to assign user because: " + e.getMessage());
		} catch (IllegalStateException e1) {
			eventResult.setSuccess(false);
			eventResult.setErrorCode("MAX_USERS_REACHED");
			eventResult.setMessage("Failed to assign user because: " + e1.getMessage());
		}

		return eventResult;
	}

}
