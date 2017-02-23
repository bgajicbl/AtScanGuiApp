package at.mtel.denza.alfresco.ws.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.jpa.Document;
import at.mtel.denza.alfresco.jpa.EntityManagerSingleton;
import at.mtel.denza.alfresco.jpa.Metadata;
import at.mtel.denza.alfresco.jpa.Subscriber;
import at.mtel.denza.alfresco.util.DateUtil;

@Path("/alfresco/metadatas")
public class MetadataWebServices {

	// Metadata za sve dokumente
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/")
	public Response getAllIds() {
		List<Metadata> x = FunctionIntegrator.getAllMetadata();
		GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
		};
		return Response.ok(list).build();
	}

	// Metadata za sve dokumente za poslati ID klijenta
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/customer/{customer}")
	public Response getAllDocuments4Customer(@PathParam("customer") int customerId) {
		List<Customer> customerList = FunctionIntegrator.getCustomer(Integer.toString(customerId));
		if (customerList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build();

		List<Metadata> x = FunctionIntegrator.getMetadata(customerId);
		GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
		};
		return Response.ok(list).build();
	}

	// Metadata za sve dokumente za poslate ID klijenta i tip dokumenta
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customer/{customer}/doctype/{doctype}")
	public Response getMetadata4CustomerDocType(@PathParam("customer") int customerId,
			@PathParam("doctype") int doctype) {
		List<Customer> customerList = FunctionIntegrator.getCustomer(Integer.toString(customerId));
		if (customerList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build();

		List<Metadata> x = FunctionIntegrator.getMetadata(customerId, doctype);
		GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
		};
		return Response.status(201).entity(list).build();
	}

	// Metadata za sve dokumente za poslate clientID, doctype i period
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/customer/{customer}/doctype/{doctype}/from/{from}/to/{to}")
	public Response getMetadata4CustomerDocTypePeriod(@PathParam("customer") int customerId,
			@PathParam("doctype") int doctype, @PathParam("from") String from, @PathParam("to") String to) {
		List<Customer> customerList = FunctionIntegrator.getCustomer(Integer.toString(customerId));
		if (customerList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build(); 
		try {
			List<Metadata> x = FunctionIntegrator.getMetadata(customerId, doctype, from, to);
			GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
			};
			return Response.status(201).entity(list).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_IMPLEMENTED).build();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/subscriber/{subscriber}")
	public Response getAllDocuments4Subscriber(@PathParam("subscriber") int subscriberId) {
		List<Subscriber> subscriberList = FunctionIntegrator.getSubscriber(Integer.toString(subscriberId));
		if (subscriberList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build();

		List<Metadata> x = FunctionIntegrator.getSubscriberMetadata(subscriberId);
		GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
		};
		return Response.ok(list).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/subscriber/{subscriber}/doctype/{doctype}")
	public Response getMetadata4SubscriberDocType(@PathParam("subscriber") int subscriberId,
			@PathParam("doctype") int doctype) {
		List<Subscriber> subscriberList = FunctionIntegrator.getSubscriber(Integer.toString(subscriberId));
		if (subscriberList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build();

		List<Metadata> x = FunctionIntegrator.getSubscriberMetadata(subscriberId, doctype);
		GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
		};
		return Response.status(201).entity(list).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/subscriber/{subscriber}/doctype/{doctype}/from/{from}/to/{to}")
	public Response getMetadata4SubscriberDocTypePeriod(@PathParam("subscriber") int subscriberId,
			@PathParam("doctype") int doctype, @PathParam("from") String from, @PathParam("to") String to) {
		List<Subscriber> subscriberList = FunctionIntegrator.getSubscriber(Integer.toString(subscriberId));
		if (subscriberList.size() == 0)
			return Response.status(Response.Status.NOT_FOUND).build(); 
		
		try {
			List<Metadata> x = FunctionIntegrator.getSubscriberMetadata(subscriberId, doctype, from, to);
			GenericEntity<List<Metadata>> list = new GenericEntity<List<Metadata>>(x) {
			};
			return Response.status(201).entity(list).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_IMPLEMENTED).build();
		}
	}

	// Upisi metadata
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insert")
	public Response insertMetadata(@QueryParam("customerId") int customerId, @QueryParam("subscriberId") String subscriberId,
			@QueryParam("fileName") String fileName, @QueryParam("nodeRef") String nodeRef, @QueryParam("period") int period,
			@QueryParam("documentType") int doctype, @QueryParam("user") int user) {
		Metadata m = new Metadata();
		// System.out.println(FunctionIntegrator.getCustomer(String.valueOf(customerId)).size());
		Customer c = FunctionIntegrator.getCustomer(String.valueOf(customerId)).get(0);
		Subscriber sub = null;
		if(subscriberId != null){
			sub = FunctionIntegrator.getSubscriber(subscriberId).get(0);
		}
		Document d = FunctionIntegrator.getDocument(doctype);
		m.setCustomer(c);
		m.setSubscriber(sub);
		m.setDocument(d);
		m.setNoderef(nodeRef);
		m.setPeriod(DateUtil.getDateFromString(String.valueOf(period)));
		// System.out.println(m);
		EntityManagerSingleton.getEntityManager().getTransaction().begin();
		EntityManagerSingleton.getEntityManager().persist(m);
		EntityManagerSingleton.getEntityManager().getTransaction().commit();
		return Response.ok(String.valueOf(m.getId())).build();
	}
}