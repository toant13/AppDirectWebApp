package com.app.dir.persistence.domain.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.dir.domain.Event;
import com.app.dir.domain.Payload;
import com.app.dir.persistence.domain.Subscription;
import com.app.dir.persistence.domain.User;

/**
 * @author toantran Data Access Object that handles persist mechanism to
 *         database
 */
@Component
public class SubscriptionDao {
	private static final Logger log = LoggerFactory
			.getLogger(SubscriptionDao.class);

	@PersistenceContext
	private EntityManager em;

	/**
	 * Creates subscription in data base
	 * 
	 * @param account
	 *            Account to create
	 * @param user
	 *            User to assign
	 */
	@Transactional
	public void createSubscription(Subscription account, User user) throws EntityExistsException{
		log.debug("Contains account: " + em.contains(account));

		// Add subscription
		if (!em.contains(account)) {
			em.persist(account);
			// Add user
			if (!em.contains(user)) {
				em.persist(user);
			} else {
				throw new EntityExistsException("User entity already exists");
			}
		} else {
			throw new EntityExistsException(
					"Subscription entity already exists");
		}

	}

	/**
	 * Removes subscription from database
	 * 
	 * @param payload
	 *            Payload with information necessary to remove subscription
	 * @throws IllegalArgumentException
	 *             Thrown when entry cannot be found in database
	 */
	public void removeSubscription(Payload payload)
			throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		try {
			TypedQuery<Subscription> subscriptionQuery = entityManager
					.createQuery("SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

			// TypedQuery<Subscription> query = entityManager.createQuery(
			// "SELECT g FROM Subscription g WHERE g.firstName=\""
			// + "DummyCreatorFirst"
			// + "\"", Subscription.class);

			Subscription subscription = subscriptionQuery.getSingleResult();

			entityManager.getTransaction().begin();
			if (subscription.getUsers().size() > 0) {
				for (User user : subscription.getUsers()) {
					TypedQuery<User> userQuery = entityManager.createQuery(
							"SELECT g FROM User g WHERE g.UserID=\""
									+ user.getUserID() + "\"", User.class);

					entityManager.remove(userQuery.getSingleResult());
				}
			}
			entityManager.remove(subscription);
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

	/**
	 * Returns a list of all the subscriptions in the database
	 * 
	 * @return List of subscriptions
	 */
	public List<Subscription> getAllAccounts() {
		TypedQuery<Subscription> query = em.createQuery(
				"SELECT g FROM Subscription g ORDER BY g.id",
				Subscription.class);
		return query.getResultList();
	}

	/**
	 * Returns list of all users assigned in the app
	 * 
	 * @return List of users
	 */
	public List<User> getAllUsers() {
		TypedQuery<User> query = em.createQuery(
				"SELECT g FROM User g ORDER BY g.UserID", User.class);
		return query.getResultList();
	}

	/**
	 * Updates the edition code of the subscription
	 * 
	 * @param payload
	 *            Payload with information necessary for edition code update
	 * @throws IllegalArgumentException
	 *             Thrown when entry cannot be found in database
	 */
	@Transactional
	public void changeEditionCode(Payload payload)
			throws IllegalArgumentException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("updateAccount method");

		try {
			TypedQuery<Subscription> subscriptionQuery = entityManager
					.createQuery("SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

			Subscription subscription = subscriptionQuery.getSingleResult();
			entityManager.getTransaction().begin();
			subscription.setEditionCode(payload.getOrder().getEditionCode());
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

	/**
	 * Assigns user to subscription
	 * 
	 * @param event
	 *            Object with necessary information to assign user
	 * @throws IllegalArgumentException
	 *             Subscription cannot be found
	 * @throws IllegalStateException
	 *             Thrown when no more users can be added because the max
	 *             capacity has been reached
	 */
	@Transactional
	public void assignUser(Event event) throws IllegalArgumentException,
			IllegalStateException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("assignUser method");

		try {
			TypedQuery<Subscription> subscriptionQuery = entityManager
					.createQuery("SELECT g FROM Subscription g WHERE g.id=\""
							+ event.getPayload().getAccount()
									.getAccountIdentifier() + "\"",
							Subscription.class);

			Subscription subscription = subscriptionQuery.getSingleResult();

			if (subscription.getEmail().equals(event.getCreator().getEmail())) {

				if (subscription.getUsers().size() < subscription.getMaxUsers()) {

					for (User user : subscription.getUsers()) {
						if (user.getEmail().equals(
								event.getPayload().getUser().getEmail())) {
							throw new IllegalArgumentException(
									"User with email already exists");
						}
					}
					log.debug("CURRENT SIZE: " + subscription.getUsers().size());
					log.debug("MAX CAPACITY: " + subscription.getMaxUsers());
					entityManager.getTransaction().begin();
					User user = new User();
					user.setFirstName(event.getPayload().getUser()
							.getFirstName());
					user.setLastName(event.getPayload().getUser().getLastName());
					user.setOpenId(event.getPayload().getUser().getOpenId());
					user.setUserID(UUID.randomUUID().toString());
					user.setEmail(event.getPayload().getUser().getEmail());
					user.setSubscription(subscription);
					entityManager.persist(user);
					entityManager.getTransaction().commit();
				} else {
					log.error("Number of users for this subscription have already reach full capacity");
					throw new IllegalStateException(
							"Number of users for this subscription have already reach full capacity");
				}
			} else {
				throw new IllegalArgumentException(
						"Do not have authorization to assign user");
			}
		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}
	}

	/**
	 * @param payload
	 *            Payload object with necessary information to unassign user
	 * @throws IllegalArgumentException
	 *             Subscription cannot be found
	 * @throws IllegalStateException
	 *             User cannot be found or There are no users to remove
	 */
	@Transactional
	public void unassignUser(Payload payload) throws IllegalArgumentException,
			IllegalStateException {
		EntityManager entityManager = em.getEntityManagerFactory()
				.createEntityManager();
		log.debug("unassignUser method");

		try {
			TypedQuery<Subscription> subscriptionQuery = entityManager
					.createQuery("SELECT g FROM Subscription g WHERE g.id=\""
							+ payload.getAccount().getAccountIdentifier()
							+ "\"", Subscription.class);

			Subscription subscription = subscriptionQuery.getSingleResult();
			User eraseUser = null;
			if (subscription.getUsers().size() > 0) {
				for (User user : subscription.getUsers()) {
					if (user.getEmail().equals(payload.getUser().getEmail())) {
						eraseUser = user;
						break;
					}
				}
				if (eraseUser != null) {
					TypedQuery<User> userQuery = entityManager.createQuery(
							"SELECT g FROM User g WHERE g.UserID=\""
									+ eraseUser.getUserID() + "\"", User.class);
					entityManager.getTransaction().begin();
					entityManager.remove(userQuery.getSingleResult());
					entityManager.getTransaction().commit();
				} else {
					throw new IllegalStateException(
							"No users match given criteria");
				}

			} else {
				throw new IllegalStateException(
						"Subscription has no users to unassign");
			}

		} catch (Exception e) {
			log.error("Error occurred during query");
			throw new IllegalArgumentException("Subscription not found");
		}

	}

}
