package com.app.dir.event.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 * 
 * Handles events relating to user unassignment from a subscription
 *
 */
public class UserUnassignmentEventProcessor implements EventProcessor {
	private static final Logger log = LoggerFactory
			.getLogger(UserUnassignmentEventProcessor.class);
	
	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#getEventType()
	 */
	@Override
	public String getEventType() {
		return "USER_UNASSIGNMENT";
	}

	/* (non-Javadoc)
	 * @see com.app.dir.event.processors.EventProcessor#processEvent(com.app.dir.domain.Event, com.app.dir.persistence.domain.dao.SubscriptionDao)
	 */
	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		EventResult eventResult = new EventResult();
		log.debug("invoking processEvent method in " + this.getClass().getName());
		
		try {
			accountDao.unassignUser(event.getPayload());
			eventResult.setSuccess(true);
			eventResult.setMessage("Successfully Assigned: "
					+ event.getPayload().getUser().getFirstName() + " " + event.getPayload().getUser().getLastName());
		} catch (IllegalArgumentException e) {
			eventResult.setSuccess(false);
			eventResult.setErrorCode("ACCOUNT_NOT_FOUND");
			eventResult.setMessage("Failed to unassign user because: " + e.getMessage());
		} catch (IllegalStateException e1) {
			eventResult.setSuccess(false);
			eventResult.setErrorCode("INVALID_RESPONSE");
			eventResult.setMessage(e1.getMessage());
		}

		return eventResult;
	}

}
