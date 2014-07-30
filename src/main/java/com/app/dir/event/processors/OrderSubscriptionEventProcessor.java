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
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.User;
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
		
		
		Subscription subscription = new Subscription();
		
		subscription.setCompanyUUID(event.getPayload().getCompany().getUuid().toString());
		subscription.setCreationDate(new Date());
		subscription.setEditionCode(event.getPayload().getOrder().getEditionCode());
		subscription.setFirstName(event.getCreator().getFirstName());
		subscription.setLastName(event.getCreator().getLastName());
		
		List<Item> itemList = event.getPayload().getOrder().getItems();
		if(itemList != null){
			for(Item item : itemList){
				if(item.getUnit().equals("USER")){
					subscription.setMaxUsers(item.getQuantity());
					break;
				}
			}
		} else {
			subscription.setMaxUsers(Integer.MAX_VALUE);
		}
		
		String accountID = UUID.randomUUID().toString();
		subscription.setId(accountID);
		
		EventResult eventResult = new EventResult();
		try {
			
			//Assign creator of subscription as a user
			User user = new User();
			user.setEmail(event.getCreator().getEmail());
			user.setFirstName(event.getCreator().getFirstName());
			user.setLastName(event.getCreator().getLastName());
			user.setOpenId(event.getCreator().getOpenId());
			user.setUserID(UUID.randomUUID().toString());
			user.setSubscription(subscription);
			
			accountDao.createSubscription(subscription, user);
			eventResult.setSuccess(true);
			eventResult.setMessage("Subscription creation successful");
			eventResult.setAccountIdentifier(accountID);
	
			
		} catch(EntityExistsException e){
			eventResult.setSuccess(false);
			eventResult.setMessage("Subscription creation failed");		
		}
		
		return eventResult;
	}

}
