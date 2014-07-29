package com.app.dir.persistence.domain.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.dir.domain.Event;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.User;

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

	public void remove(Event event) throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		try {
			TypedQuery<Subscription> query = entityManager.createQuery(
					"SELECT g FROM Subscription g WHERE g.id=\""
							+ event.getPayload().getAccount()
									.getAccountIdentifier() + "\"",
					Subscription.class);

//			TypedQuery<Subscription> query = entityManager.createQuery(
//					"SELECT g FROM Subscription g WHERE g.firstName=\""
//							+ event.getCreator().getFirstName() + "\"",
//					Subscription.class);

			Subscription result = query.getSingleResult();

			entityManager.getTransaction().begin();
			entityManager.remove(result);
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

	public List<Subscription> getAllAccounts() {
		TypedQuery<Subscription> query = em.createQuery(
				"SELECT g FROM Subscription g ORDER BY g.id",
				Subscription.class);
		return query.getResultList();
	}
	
	public List<User> getAllUsers() {
		TypedQuery<User> query = em.createQuery(
				"SELECT g FROM User g ORDER BY g.UserID",
				User.class);
		return query.getResultList();
	}

	@Transactional
	public void changeEditionCode(Event event) throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("updateAccount method");

		try {
			 TypedQuery<Subscription> query = entityManager.createQuery(
			 "SELECT g FROM Subscription g WHERE g.id=\""
			 + event.getPayload().getAccount()
			 .getAccountIdentifier() + "\"",
			 Subscription.class);

			// TypedQuery<Subscription> query = entityManager.createQuery(
			// "SELECT g FROM Subscription g WHERE g.firstName=\""
			// + event.getCreator().getFirstName() + "\"",
			// Subscription.class);

			Subscription result = query.getSingleResult();
			entityManager.getTransaction().begin();
			result.setEditionCode(event.getPayload().getOrder()
					.getEditionCode());
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

	@Transactional
	public void assignUser(Event event) throws IllegalArgumentException, IllegalStateException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("assignUser method");

		try {
			TypedQuery<Subscription> query = entityManager.createQuery(
					"SELECT g FROM Subscription g WHERE g.id=\""
							+ event.getPayload().getAccount()
									.getAccountIdentifier() + "\"",
					Subscription.class);
			
//			TypedQuery<Subscription> query = entityManager.createQuery(
//					"SELECT g FROM Subscription g WHERE g.firstName=\""
//							+ event.getCreator().getFirstName() + "\"",
//					Subscription.class);
			
			Subscription result = query.getSingleResult();

			log.debug("Transaction beginning");

			if (result.getUsers().size() < result.getMaxUsers()) {
				log.debug("CURRENT SIZE: " + result.getUsers().size());
				log.debug("MAX CAPACITY: " + result.getMaxUsers());
				entityManager.getTransaction().begin();
				User user = new User();
				user.setFirstName(event.getPayload().getUser().getFirstName());
				user.setLastName(event.getPayload().getUser().getLastName());
				user.setOpenId(event.getPayload().getUser().getOpenId());
				user.setUserID(UUID.randomUUID().toString());
				user.setEmail(event.getPayload().getUser().getEmail());
				user.setSubscription(result);
				entityManager.persist(user);
				entityManager.getTransaction().commit();
			} else {
				log.error("Number of users for this subscription have already reach full capacity");
				throw new IllegalStateException(
						"Number of users for this subscription have already reach full capacity");
			}
		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}
	}

}
