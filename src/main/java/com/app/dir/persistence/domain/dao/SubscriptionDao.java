package com.app.dir.persistence.domain.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.dir.domain.Event;
import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.Subscription;

@Component
public class SubscriptionDao {
	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionDao.class);

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void persist(Subscription account) {
		log.debug("Contains account: " + em.contains(account));
		if (!em.contains(account)) {
			em.persist(account);

		}

	}

	public void remove(Subscription account) {
		em.remove(account);
	}

	public List<Subscription> getAllAccounts() {
		TypedQuery<Subscription> query = em.createQuery(
				"SELECT g FROM Subscription g ORDER BY g.id",
				Subscription.class);
		return query.getResultList();
	}

	@Transactional
	public void changeAccount(Event event)
			throws IllegalArgumentException {
		EntityManager eme = em.getEntityManagerFactory().createEntityManager();
		log.debug("updateAccount method");
		TypedQuery<Subscription> query = eme
				.createQuery(
						"SELECT g FROM Subscription g WHERE g.id=\""
								+ event.getPayload().getAccount().getAccountIdentifier() + "\"", Subscription.class);
//		TypedQuery<Subscription> query = eme.createQuery("SELECT g FROM Subscription g WHERE g.firstName=\""+ event.getCreator().getFirstName() + "\"", Subscription.class);

		
		Subscription result = query.getSingleResult();

		if (result != null) {
			eme.getTransaction().begin();
			result.setEditionCode(event.getPayload().getOrder().getEditionCode());
			eme.getTransaction().commit();
		} else {
			throw new IllegalArgumentException("Account not found");
		}

	}

}
