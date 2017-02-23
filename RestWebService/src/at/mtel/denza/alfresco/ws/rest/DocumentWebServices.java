package at.mtel.denza.alfresco.ws.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;

@Path("/alfresco")
public class DocumentWebServices { 

	@GET
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	@Path("/documents/invoice")
	public Response getInvoceByCustomerAndSubscriber(@QueryParam("customerId") String customerId,
			@QueryParam("subscriberId") String subscriberId, @QueryParam("invoiceNo") String invoiceNo,
			@QueryParam("alf_ticket") String ticket) {

		String ebill = customerId + SEPARATOR + invoiceNo + EBILL_SUFFIX;
		String post = customerId + SEPARATOR + invoiceNo + POST_SUFFIX;

		if (subscriberId != null && subscriberId.length() > 1) {
			ebill = customerId + SEPARATOR + subscriberId + SEPARATOR + invoiceNo + EBILL_SUFFIX;
			post = customerId + SEPARATOR + subscriberId + SEPARATOR + invoiceNo + POST_SUFFIX;
		} 

		//System.out.println(ebill);  

		Session session = null;  
		try {
			session = getSession(ticket);
		} catch (CmisUnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		QueryStatement qs = session.createQueryStatement(QUERY);
		qs.setString(1, ebill);

		ItemIterable<QueryResult> results = session.query(qs.toQueryString(), false);
		// if there is not eBill we are searching for post
		if (results.getTotalNumItems() == 0) {

			qs.setString(1, post);
			results = session.query(qs.toQueryString(), false);
		}
		// if there is not post either we are returning HTTP 404
		if (results.getTotalNumItems() == 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return createResponse(session, results);
	}

	@GET
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	@Path("/documents/een")
	public Response getEenByNumber(@QueryParam("customerId") String customerId,
			@QueryParam("subscriberId") String subscriberId, @QueryParam("invoiceNo") String invoiceNo,
			@QueryParam("alf_ticket") String ticket) {

		String een = customerId + SEPARATOR + invoiceNo + EEN_SUFFIX;
		
		if (subscriberId != null && subscriberId.length() > 1) {
			een = customerId + SEPARATOR + subscriberId + SEPARATOR + invoiceNo + EEN_SUFFIX;
		}

		Session session = null;
		try {
			session = getSession(ticket);
		} catch (CmisUnauthorizedException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		QueryStatement qs = session.createQueryStatement(QUERY);
		qs.setString(1, een);
		//System.out.println("een: " + een);
		ItemIterable<QueryResult> results = session.query(qs.toQueryString(), false);
		//System.out.println("res: " + results.getTotalNumItems());

		// if there is no een we are returning HTTP 404
		if (results.getTotalNumItems() == 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return createResponse(session, results);
	}

	private Session getSession(String ticket) throws CmisUnauthorizedException {
		SessionFactory factory = SessionFactoryImpl.newInstance();

		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.USER, "ROLE_TICKET");
		parameter.put(SessionParameter.PASSWORD, ticket);
		parameter.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		// get first repository and create session
		Repository repository = factory.getRepositories(parameter).get(0);
		Session session = repository.createSession();

		return session;
	}

	private Response createResponse(Session session, ItemIterable<QueryResult> results) {
		Object value = null;
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				value = property.getFirstValue();
			}
		}
		Document file = session.getLatestDocumentVersion((String) value);
		ContentStream contentStream = file.getContentStream();
		ResponseBuilder responseBuilder = Response.ok((Object) contentStream.getStream());
		responseBuilder.header("content-disposition", "attachment; filename = " + file.getName());

		return responseBuilder.build();
	}

	public static String ATOMPUB_URL = "http://127.0.0.1:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	public static String QUERY = "SELECT alfcmis:nodeRef FROM cmis:document WHERE cmis:name = ?";
	public static String EBILL_SUFFIX = "_eBill.PDF";
	public static String POST_SUFFIX = "_post.PDF";
	public static String EEN_SUFFIX = "_een.PDF";
	public static String SEPARATOR = "-";
}
