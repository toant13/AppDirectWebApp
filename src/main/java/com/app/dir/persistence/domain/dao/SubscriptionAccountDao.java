package com.app.dir.persistence.domain.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void remove(SubscriptionAccount account) {
		em.remove(account);
	}

	public List<SubscriptionAccount> getAllAccounts() {
		TypedQuery<SubscriptionAccount> query = em.createQuery(
				"SELECT g FROM SubscriptionAccount g ORDER BY g.id", SubscriptionAccount.class);
		return query.getResultList();
	}

}
