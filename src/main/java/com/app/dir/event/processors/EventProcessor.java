package com.app.dir.event.processors;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

/**
 * @author toantran
 *	
 *	Interface of methods that process the Event xml
 */
public interface EventProcessor {
	
	
	/**
	 * Returns event type for given processor
	 * @return String value of event type
	 */
	String getEventType();
	
	
	/**
	 * Processes Event xml
	 * @param event event xml to be used to process
	 * @param accountDao Subscription Data Access Object used to handling data transactions
	 * @return Result of event process
	 */
	EventResult processEvent(Event event, SubscriptionDao accountDao);
}
