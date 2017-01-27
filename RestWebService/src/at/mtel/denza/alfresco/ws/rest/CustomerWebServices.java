package at.mtel.denza.alfresco.ws.rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.jpa.EntityManagerSingleton;
import at.mtel.denza.alfresco.jpa.Subscriber;
import at.mtel.denza.alfresco.jpa.User;

@Path("/alfresco/customers")
public class CustomerWebServices {
	Customer c = null;

	private static final Logger logger = LogManager.getLogger("RestWebService");

	// Pozovi metodu koja vraca sve klijente
	/*@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/")
	public Response getUsers() {
		// System.out.println(logger.getLevel());
		// logger.warn("Entering application.");
		List<Customer> customerList = FunctionIntegrator.getAllCustomers();
		if (customerList.size() == 0)
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

		return Response.ok(customerList).build();
	}*/

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response getUser(@PathParam("id") String customerId) {
		List<Customer> customerList = FunctionIntegrator.getCustomer(customerId);
		c = null;
		if (customerList.size() > 0) {
			c = customerList.get(0);
			// System.out.println("size: "+customerList.size());
		} else
			return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(c).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/insert/{id}")
	public Response insertCustromer(@PathParam("id") String customerId) {
		c = new Customer();
		c.setCustomerId(customerId);
		c.setMsisdn(0L);
		try {
			EntityManagerSingleton.getEntityManager().getTransaction().begin();
			EntityManagerSingleton.getEntityManager().persist(c);
			EntityManagerSingleton.getEntityManager().getTransaction().commit();

			logger.warn("Customer created: " + customerId);
		} catch (RollbackException re) {
			for (Throwable t = re.getCause(); t != null; t = t.getCause()) {
				// System.out.println("Exc777:" + t.getCause());
				if (t instanceof org.postgresql.util.PSQLException)
					return Response.status(Response.Status.CONFLICT).build();
			}
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		return Response.ok(String.valueOf(c.getId())).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/insert/{custId}/subscriber/{subsId}")
	public Response insertCustromerAndSubscriber(@PathParam("custId") String customerId,
			@PathParam("subsId") String subscriberId) {
		// provjeriti da li postoji customer
		List<Customer> customerList = FunctionIntegrator.getCustomer(customerId);
		List<Subscriber> subscriber = FunctionIntegrator.getSubscriber(subscriberId);

		if (customerList.size() > 0) {//customer vec postoji, dodati subscribera
			if(subscriber.size() > 0){//subscriber veæ postoji, conflict
				return Response.status(Response.Status.CONFLICT).build();
			}else{//dodati subscribera
				c = customerList.get(0);
				List<Subscriber> subscriberList = c.getSubscribers();
				Subscriber subscr = new Subscriber();
				subscr.setSubscriberId(subscriberId);
				subscr.setCustomer(c);
				subscriberList.add(subscr);
				c.setSubscribers(subscriberList);
			}
		} else {//dodati i customera i subscribera
			c = new Customer();
			c.setCustomerId(customerId);
			List<Subscriber> subscriberList = new ArrayList<>();
			Subscriber subscr = new Subscriber();
			subscr.setSubscriberId(subscriberId);
			subscr.setCustomer(c);
			subscriberList.add(subscr);
			c.setSubscribers(subscriberList);
		}
		// kasnije treba promijeniti da ide iz PathParam
		c.setMsisdn(0L);
		try {
			EntityManagerSingleton.getEntityManager().getTransaction().begin();
			EntityManagerSingleton.getEntityManager().persist(c);
			EntityManagerSingleton.getEntityManager().getTransaction().commit();

			logger.warn("Customer created: " + customerId);
		} catch (RollbackException re) {
			for (Throwable t = re.getCause(); t != null; t = t.getCause()) {
				// System.out.println("Exc777:" + t.getCause());
				if (t instanceof org.postgresql.util.PSQLException)
					return Response.status(Response.Status.CONFLICT).build();
			}
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		return Response.ok(String.valueOf(c.getId())).build();
	}

	// Pozovi metodu koja vraca sve msisdn za korisnika
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/msisdn")
	public Response getMsisdn(@QueryParam("id") String customerId) {
		return Response.ok(FunctionIntegrator.getCustomer(customerId)).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/delete/{id}")
	public Response deleteUser(@PathParam("id") String customerId, @QueryParam("u") String username,
			@QueryParam("p") String password) {
		// provjera da li je korisnik unio username i password
		User x = FunctionIntegrator.checkUser(username, password);
		if (x == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();

		c = null;
		List<Customer> customerList = FunctionIntegrator.getCustomer(customerId);
		if (customerList.size() > 0)
			c = customerList.get(0);
		else
			return Response.status(Response.Status.NOT_FOUND).build();

		try {
			EntityManagerSingleton.getEntityManager().getTransaction().begin();
			EntityManagerSingleton.getEntityManager().remove(c);
			EntityManagerSingleton.getEntityManager().getTransaction().commit();

			logger.warn("Customer deleted: " + customerId + " user: " + username);
		} catch (RollbackException re) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		return Response.ok(String.valueOf(c.getId())).build();
	}
}