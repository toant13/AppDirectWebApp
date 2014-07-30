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

import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.User;

@Component
public class SubscriptionDao {
	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionDao.class);

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void createSubscription(Subscription account, User user) {
		log.debug("Contains account: " + em.contains(account));

		// Add subscription
		if (!em.contains(account)) {
			em.persist(account);

		}

		// Add user
		if (!em.contains(user)) {
			em.persist(user);
		}
	}

	public void removeSubscription(Payload payload)
			throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		try {
			TypedQuery<Subscription> query = entityManager.createQuery(
					"SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

//			TypedQuery<Subscription> query = entityManager.createQuery(
//					"SELECT g FROM Subscription g WHERE g.firstName=\""
//							+ "DummyCreatorFirst"
//							+ "\"", Subscription.class);
			
			Subscription result = query.getSingleResult();

			entityManager.getTransaction().begin();
			if (result.getUsers().size() > 0) {
				for (User user : result.getUsers()) {
					TypedQuery<User> userQuery = entityManager.createQuery(
							"SELECT g FROM User g WHERE g.UserID=\""
									+ user.getUserID() + "\"", User.class);
					
					entityManager.remove(userQuery.getSingleResult());
				}
			} 
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
				"SELECT g FROM User g ORDER BY g.UserID", User.class);
		return query.getResultList();
	}

	@Transactional
	public void changeEditionCode(Payload payload)
			throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("updateAccount method");

		try {
			TypedQuery<Subscription> query = entityManager.createQuery(
					"SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

			Subscription result = query.getSingleResult();
			entityManager.getTransaction().begin();
			result.setEditionCode(payload.getOrder().getEditionCode());
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

	@Transactional
	public void assignUser(Payload payload) throws IllegalArgumentException,
			IllegalStateException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("assignUser method");

		try {
			TypedQuery<Subscription> query = entityManager.createQuery(
					"SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

			Subscription result = query.getSingleResult();

			log.debug("Transaction beginning");

			if (result.getUsers().size() < result.getMaxUsers()) {
				log.debug("CURRENT SIZE: " + result.getUsers().size());
				log.debug("MAX CAPACITY: " + result.getMaxUsers());
				entityManager.getTransaction().begin();
				User user = new User();
				user.setFirstName(payload.getUser().getFirstName());
				user.setLastName(payload.getUser().getLastName());
				user.setOpenId(payload.getUser().getOpenId());
				user.setUserID(UUID.randomUUID().toString());
				user.setEmail(payload.getUser().getEmail());
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
