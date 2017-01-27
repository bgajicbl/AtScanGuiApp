package at.mtel.denza.alfresco.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.persistence.exceptions.DatabaseException;

public class EntityManagerSingleton {

	public static EntityManager getEntityManager() {
		if (em == null) {
			try {
				em = Persistence.createEntityManagerFactory("RestWebService").createEntityManager();
			} catch (PersistenceException e) {
				 //napraviti error response
				throw new WebApplicationException(e.getMessage(),
						Response.Status.SERVICE_UNAVAILABLE);
			}
		}
		return em;
	}

	private static EntityManager em = null;
}
