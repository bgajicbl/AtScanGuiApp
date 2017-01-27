package at.mtel.denza.alfresco.ws.rest;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.jpa.Document;
import at.mtel.denza.alfresco.jpa.EntityManagerSingleton;
import at.mtel.denza.alfresco.jpa.Metadata;
import at.mtel.denza.alfresco.jpa.Subscriber;
import at.mtel.denza.alfresco.jpa.User;
import at.mtel.denza.alfresco.util.DateUtil;

public class FunctionIntegrator {

	// Svi klijenti koji postoje u tabeli customers
	public static List<Customer> getAllCustomers() {
		TypedQuery<Customer> query = EntityManagerSingleton.getEntityManager().createQuery(allCustomersQuery,
				Customer.class);
		return query.getResultList();
	}

	// Svi msisdn za customer id
	public static List<Customer> getCustomer(String id) {
		TypedQuery<Customer> query = EntityManagerSingleton.getEntityManager().createQuery(customerQuery,
				Customer.class);
		query.setParameter("customer", id);
		return query.getResultList();
	}

	public static List<Subscriber> getAllSubscribers() {
		TypedQuery<Subscriber> query = EntityManagerSingleton.getEntityManager().createQuery(allSubscribersQuery,
				Subscriber.class);
		return query.getResultList();
	}

	public static List<Subscriber> getSubscriber(String id) {
		TypedQuery<Subscriber> query = EntityManagerSingleton.getEntityManager().createQuery(subscriberQuery,
				Subscriber.class);
		query.setParameter("subscriber", id);
		return query.getResultList();
	}

	// sve iz tabele metadata
	public static List<Metadata> getAllMetadata() {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager().createQuery(allMetadataQuery,
				Metadata.class);
		return query.getResultList();
	}

	// Metadata za sve dokumente jednog klijenta
	public static List<Metadata> getMetadata(int customerId) {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager().createQuery(metadataQuery,
				Metadata.class);
		query.setParameter("customer", String.valueOf((customerId)));
		return query.getResultList();
	}

	// Metadata za sve dokumente za poslate customerId i doctype
	public static List<Metadata> getMetadata(int customerId, int doctype) {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager().createQuery(metadataDocTypeQuery, Metadata.class);
		query.setParameter("customer", String.valueOf((customerId)));
		query.setParameter("doctype", doctype);
		return query.getResultList();
	}

	// Metadata za sve dokumente za poslate customerId, doctype i period
	public static List<Metadata> getMetadata(int customerId, int doctype, String from, String to)
			throws IllegalArgumentException {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager()
				.createQuery(metadataDocTypeAndPeriodQuery, Metadata.class);
		Date fDate = DateUtil.getDateFromString(from);
		Date toDate = DateUtil.getDateFromString(to);
		query.setParameter("customer", String.valueOf(customerId));
		query.setParameter("doctype", doctype);
		query.setParameter("from", fDate);
		query.setParameter("to", toDate);
		return query.getResultList();
	}

	///////////////////////////////////////////////////////////////////////////
	// Metadata za sve dokumente jednog klijenta
	public static List<Metadata> getSubscriberMetadata(int subscriberId) {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager().createQuery(metadataSubscriberQuery,
				Metadata.class);
		query.setParameter("subscriber", String.valueOf((subscriberId)));
		return query.getResultList();
	}

	// Metadata za sve dokumente za poslate customerId i doctype
	public static List<Metadata> getSubscriberMetadata(int subscriberId, int doctype) {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager()
				.createQuery(metadataSubscriberDocTypeQuery, Metadata.class);
		query.setParameter("subscriber", String.valueOf((subscriberId)));
		query.setParameter("doctype", doctype);
		return query.getResultList();
	}

	// Metadata za sve dokumente za poslate customerId, doctype i period
	public static List<Metadata> getSubscriberMetadata(int subscriberId, int doctype, String from, String to)
			throws IllegalArgumentException {
		TypedQuery<Metadata> query = EntityManagerSingleton.getEntityManager()
				.createQuery(metadataSubscriberDocTypeAndPeriodQuery, Metadata.class);
		Date fDate = DateUtil.getDateFromString(from);
		Date toDate = DateUtil.getDateFromString(to);
		query.setParameter("subscriber", String.valueOf(subscriberId));
		query.setParameter("doctype", doctype);
		query.setParameter("from", fDate);
		query.setParameter("to", toDate);
		return query.getResultList();
	}

	// Svi tipovi dokumenata
	public static List<Document> getAllDocuments() {
		TypedQuery<Document> query = EntityManagerSingleton.getEntityManager().createQuery(allDocumentsQuery,
				Document.class);
		return query.getResultList();
	}

	// Tip dokumenta za ID iz tabele document_types
	public static Document getDocument(int documentType) {
		TypedQuery<Document> query = EntityManagerSingleton.getEntityManager().createQuery(documentsTypeQuery,
				Document.class);
		query.setParameter("doctype", documentType);
		return query.getResultList().get(0);
	}

	// Svi korisnici
	public static List<User> getAllUsers() {
		TypedQuery<User> query = EntityManagerSingleton.getEntityManager().createQuery(allUsersQuery, User.class);
		return query.getResultList();
	}

	// Da li klijent postoji u bazi
	public static User checkUser(String username, String password) {
		TypedQuery<User> query = EntityManagerSingleton.getEntityManager().createQuery(checkUserQuery, User.class);
		query.setParameter("u", username);
		query.setParameter("p", password);
		if (query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}

	public static <T> void insert(T t) {
		EntityManager em = EntityManagerSingleton.getEntityManager();
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
	}

	public static <T> void update(T t) {
		EntityManager em = EntityManagerSingleton.getEntityManager();
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
	}

	private static String allCustomersQuery = "SELECT c FROM Customer c";
	private static String customerQuery = "SELECT c FROM Customer c WHERE c.customerId = :customer";
	private static String allSubscribersQuery = "SELECT s FROM Subscriber s";
	private static String subscriberQuery = "SELECT s FROM Subscriber s WHERE s.subscriberId = :subscriber";
	private static String allMetadataQuery = "SELECT m FROM Metadata m order by m.id ASC";
	private static String metadataQuery = "SELECT m FROM Metadata m WHERE m.customer.customerId=:customer ORDER BY m.id ASC";
	private static String metadataDocTypeQuery = "SELECT m FROM Metadata m WHERE m.customer.customerId=:customer AND m.document.id = :doctype";
	private static String metadataDocTypeAndPeriodQuery = "SELECT m FROM Metadata m WHERE m.customer.customerId=:customer AND m.document.id = :doctype AND m.period >= :from AND m.period < :to";
	private static String metadataSubscriberQuery = "SELECT m FROM Metadata m WHERE m.subscriber.subscriberId=:subscriber ORDER BY m.id ASC";
	private static String metadataSubscriberDocTypeQuery = "SELECT m FROM Metadata m WHERE m.subscriber.subscriberId=:subscriber AND m.document.id = :doctype";
	private static String metadataSubscriberDocTypeAndPeriodQuery = "SELECT m FROM Metadata m WHERE m.subscriber.subscriberId=:subscriber AND m.document.id = :doctype AND m.period >= :from AND m.period < :to";

	private static String allDocumentsQuery = "SELECT d FROM Document d order by d.id ASC";
	private static String documentsTypeQuery = "SELECT d FROM Document d WHERE d.id = :doctype";
	private static String checkUserQuery = "SELECT u FROM User u WHERE u.username = :u AND u.password = :p";
	private static String allUsersQuery = "SELECT u FROM User u";
}