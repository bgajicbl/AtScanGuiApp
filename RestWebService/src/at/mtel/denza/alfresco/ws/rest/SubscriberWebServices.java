package at.mtel.denza.alfresco.ws.rest;

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

import at.mtel.denza.alfresco.jpa.EntityManagerSingleton;
import at.mtel.denza.alfresco.jpa.Subscriber;
import at.mtel.denza.alfresco.jpa.User;

@Path("/alfresco/subscribers")
public class SubscriberWebServices {
	Subscriber c = null;

	private static final Logger logger = LogManager.getLogger("RestWebService");

	// Pozovi metodu koja vraca sve klijente
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/")
	public Response getUsers() {

		List<Subscriber> sList = FunctionIntegrator.getAllSubscribers();
		if (sList.size() == 0)
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

		return Response.ok(sList).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response getUser(@PathParam("id") String subId) {
		List<Subscriber> sList = FunctionIntegrator.getSubscriber(subId);
		c = null;
		if (sList.size() > 0){
			c = sList.get(0);
		}
		else
			return Response.status(Response.Status.NOT_FOUND).build();
		
		return Response.ok(c).build(); 
	}

	/*@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/insert/{id}")
	public Response insertUser(@PathParam("id") String subId) {
		c = new Subscriber();
		c.setSubscriberId(subId);
		// kasnije treba promijeniti da ide iz PathParam
		//c.setMsisdn(123L);
		try {
			EntityManagerSingleton.getEntityManager().getTransaction().begin();
			EntityManagerSingleton.getEntityManager().persist(c);
			EntityManagerSingleton.getEntityManager().getTransaction().commit();

			logger.warn("sub created: " + subId);
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
	}*/

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/delete/{id}")
	public Response deleteUser(@PathParam("id") String subId, @QueryParam("u") String username,
			@QueryParam("p") String password) {
		// provjera da li je korisnik unio username i password
		User x = FunctionIntegrator.checkUser(username, password);
		if (x == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();

		c = null;
		List<Subscriber> sList = FunctionIntegrator.getSubscriber(subId);
		if (sList.size() > 0)
			c = sList.get(0);
		else
			return Response.status(Response.Status.NOT_FOUND).build();

		try {
			EntityManagerSingleton.getEntityManager().getTransaction().begin();
			EntityManagerSingleton.getEntityManager().remove(c);
			EntityManagerSingleton.getEntityManager().getTransaction().commit();

			logger.warn("Subscriber deleted: " + subId + " user: " + username);
		} catch (RollbackException re) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		return Response.ok(String.valueOf(c.getId())).build();
	}
}