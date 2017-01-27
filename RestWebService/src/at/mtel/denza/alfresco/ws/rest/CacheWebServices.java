package at.mtel.denza.alfresco.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.mtel.denza.alfresco.jpa.EntityManagerSingleton;

@Path("/alfresco")
public class CacheWebServices {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON }) 
	@Path("/cache/refresh")
	public Response cacheRefresh() {
		
		EntityManagerSingleton.getEntityManager().clear();
		
		return Response.ok("Cleared!").build();
	}

}
