package com.app.dir.event.processors;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dir.domain.Event;
import com.app.dir.domain.EventResult;
import com.app.dir.domain.Item;
import com.app.dir.event.EventHandler;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.dao.SubscriptionDao;

public class OrderSubscriptionEventProcessor implements EventProcessor {
	private static final Logger log = LoggerFactory
			.getLogger(OrderSubscriptionEventProcessor.class);
	
	@Override
	public String getEventType() {
		return "SUBSCRIPTION_ORDER";
	}

	@Override
	public EventResult processEvent(Event event, SubscriptionDao accountDao) {
		log.debug("processing order subscription");
		
		
		Subscription account = new Subscription();
		
		account.setCompanyUUID(event.getPayload().getCompany().getUuid().toString());
		account.setCreationDate(new Date());
		account.setEditionCode(event.getPayload().getOrder().getEditionCode());
		account.setFirstName(event.getCreator().getFirstName());
		account.setLastName(event.getCreator().getLastName());
		
		List<Item> itemList = event.getPayload().getOrder().getItems();
		if(itemList != null){
			for(Item item : itemList){
				if(item.getUnit().equals("USER")){
					account.setMaxUsers(item.getQuantity());
					break;
				}
			}
		}
		
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
