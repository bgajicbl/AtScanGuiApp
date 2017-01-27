package at.mtel.denza.alfresco.ws.rest;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.mtel.denza.alfresco.jpa.Document;

@Path("/alfresco")
public class DocumentTypeWebServices {

	// Lista svih tipova dokumenata iz tabele document_types
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/documents")
	public Response getAllDocuments() {
		List<Document> x = FunctionIntegrator.getAllDocuments();
		GenericEntity<List<Document>> list = new GenericEntity<List<Document>>(x) {
		};
		return Response.ok(list).build();
	}

	// Dokumenat za id iz tabele document_types
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/documents/{id}")
	public Response getDocuments( @PathParam("id") int id) {
		Document x = FunctionIntegrator.getDocument(id);
		return Response.ok(x).build();
	}

	// Upisi novi tip dokumenta u tabelu document_types
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/documents/insert")
	public String insertMetadata(@FormParam("documentType") String documentType) {
		Document doc = new Document();
		doc.setDocument(documentType);
		FunctionIntegrator.insert(doc);
		return doc.toString();
	}
}
