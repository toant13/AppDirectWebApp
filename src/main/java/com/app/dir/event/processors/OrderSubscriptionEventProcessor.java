package com.app.dir.event.processors;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class OrderSubscriptionEventProcessor implements EventProcessor {

	@Override
	public String getEventType() {
		return "SUBSCRIPTION_ORDER";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		Subscription account = new Subscription();
		
		account.setCompanyUUID(event.getPayload().getCompany().getUuid().toString());
		account.setCreationDate(new Date());
		account.setEditionCode(event.getPayload().getOrder().getEditionCode());
		account.setFirstName(event.getCreator().getFirstName());
		account.setLastName(event.getCreator().getLastName());
		
		String accountID = UUID.randomUUID().toString();
		account.setId(accountID);
		
		EventResult eventResult = new EventResult();
		try {
			accountDao.persist(account);
			eventResult.setSuccess(true);
			eventResult.setMessage("Account creation successful");
			eventResult.setAccountIdentifier(accountID);
		} catch(EntityExistsException e){
			eventResult.setSuccess(false);
			eventResult.setMessage("Account creation failed");		
		}
		
		return eventResult;
	}

}
