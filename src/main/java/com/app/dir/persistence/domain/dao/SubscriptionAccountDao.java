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
import com.app.dir.persistence.domain.SubscriptionAccount;

@Component
public class SubscriptionAccountDao {
	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionAccountDao.class);

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void persist(SubscriptionAccount account) {
		log.debug("Contains account: " + em.contains(account));
		if (!em.contains(account)) {
			em.persist(account);

		}

	}

	public void remove(SubscriptionAccount account) {
		em.remove(account);
	}

	public List<SubscriptionAccount> getAllAccounts() {
		TypedQuery<SubscriptionAccount> query = em.createQuery(
				"SELECT g FROM SubscriptionAccount g ORDER BY g.id",
				SubscriptionAccount.class);
		return query.getResultList();
	}

	@Transactional
	public void changeAccount(Event event)
			throws IllegalArgumentException {
		EntityManager eme = em.getEntityManagerFactory().createEntityManager();
		log.debug("updateAccount method");
		TypedQuery<SubscriptionAccount> query = eme
				.createQuery(
						"SELECT g FROM SubscriptionAccount g WHERE g.id=\""
								+ event.getPayload().getAccount().getAccountIdentifier() + "\"", SubscriptionAccount.class);
//		TypedQuery<SubscriptionAccount> query = eme
//		.createQuery(
//				"SELECT g FROM SubscriptionAccount g WHERE g.firstName=\""
//						+ event.getCreator().getFirstName() + "\"", SubscriptionAccount.class);

		
		SubscriptionAccount result = query.getSingleResult();

		if (result != null) {
			eme.getTransaction().begin();
			result.setEditionCode(event.getPayload().getOrder().getEditionCode());
			eme.getTransaction().commit();
		} else {
			throw new IllegalArgumentException("Account not found");
		}

	}

}
