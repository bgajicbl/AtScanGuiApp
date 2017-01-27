package at.mtel.denza.alfresco.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.mtel.denza.alfresco.jpa.User;

@Path("/alfresco")
public class UserWebServices {

	// Pozovi metodu koja provjerava da li postoji korisik
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/users/check")
	public Response checkUser(@QueryParam("u") String username, @QueryParam("p") String password) {
		User x = FunctionIntegrator.checkUser(username, password);
		
			return Response.ok(x).build(); 
		
		
	}

	// Pozovi metodu koja vraca sve korisnike
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/users")
	public Response getUsers() {
		return Response.ok(FunctionIntegrator.getAllUsers()).build();
	}
}