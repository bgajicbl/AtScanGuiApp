package at.mtel.denza.alfresco.scan.pojo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.jpa.Document;
import at.mtel.denza.alfresco.jpa.Metadata;
import at.mtel.denza.alfresco.jpa.User;

public class ListUtil {

	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget target = client.target(getBaseURI()).path("rest").path("alfresco");
	
	private static void setTarget() {
		target = client.target(getBaseURI()).path("rest").path("alfresco");
	}

	public static <T> List<T> genericGetFromWebService(String path, T t) {
		List<T> lista = new ArrayList<T>();
		String plainAnswer = target.path(path).request().accept(MediaType.APPLICATION_JSON).get(String.class);
		JSONArray jArray = new JSONArray(plainAnswer);
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject json = jArray.getJSONObject(i);
			t = (T) new Gson().fromJson(json.toString(), t.getClass());
			lista.add(t);
		}
		return lista;
	}
	
	// HashMapa za parametre!
	public static <T> List<T> genericGetFromWebService(String path, T t, HashMap<String, String> params) {
		List<T> lista = new ArrayList<T>(); 
		target = target.path(path);
		for(String key : params.keySet()) {
			target = target.queryParam(key, params.get(key));
		}
		//System.out.println("target: "+target);

		String plainAnswer = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
		setTarget();

		if(plainAnswer.length() == 0) {
			return null;
		} 		
		else if(plainAnswer.startsWith("[")) {
			JSONArray jArray = new JSONArray(plainAnswer);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json = jArray.getJSONObject(i);
				t = (T) new Gson().fromJson(json.toString(), t.getClass());
				lista.add(t);
			}
		} 
		else if(plainAnswer.startsWith("{")) {
			JSONObject json = new JSONObject(plainAnswer);
			t = (T) new Gson().fromJson(json.toString(), t.getClass());
			lista.add(t);
		}
		return lista;
	}
	
	
	public static List<Metadata> getAllMetadata(List<Metadata> lista) {
		String plainAnswer = target.path("metadatas").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		JSONArray jArray = new JSONArray(plainAnswer);
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject meta = jArray.getJSONObject(i);
			Metadata m = new Gson().fromJson(meta.toString(), Metadata.class);
			lista.add(m);
		}
		return lista;
	}

	public static List<Document> getDocumentTypes(List<Document> l) {
		String plainAnswer = target.path("documents").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		JSONArray jArray = new JSONArray(plainAnswer);
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jo = jArray.getJSONObject(i);
			Document d =  new Gson().fromJson(jo.toString(), Document.class);
			l.add(d);
		}
		return l;
	}

	public static User checkUser(String username, String password) {
		String plainAnswer = target.path("users/check").queryParam("u", username).queryParam("p", password).
				request().accept(MediaType.APPLICATION_JSON).get(String.class);
		if(plainAnswer.equals("") || plainAnswer.equalsIgnoreCase("null"))
			return null;
		JSONObject jo = new JSONObject(plainAnswer); 
		User up = new Gson().fromJson(jo.toString(), User.class);
		return up;
	}
	
	public static List<User> getAllUsers(List<User> l) {
		String plainAnswer = target.path("users").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		JSONArray jArray = new JSONArray(plainAnswer);
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jo = jArray.getJSONObject(i);
			User up = new Gson().fromJson(jo.toString(), User.class);
			l.add(up);
		}
		return l;
	}

	public static List<Customer> getAllCustomers() {
		List<Customer> l = new ArrayList<>();
		String plainAnswer = target.path("customers").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		JSONArray jArray = new JSONArray(plainAnswer);
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jo = jArray.getJSONObject(i);
			Customer c = new Gson().fromJson(jo.toString(), Customer.class);
			l.add(c);
		}
		return l;
	}
	
	public static URI getBaseURI() {
		return UriBuilder.fromPath("http://127.0.0.1:9090/RestWebService/").build();
	}
}