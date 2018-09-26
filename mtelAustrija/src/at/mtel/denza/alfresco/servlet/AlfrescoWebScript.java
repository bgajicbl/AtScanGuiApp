package at.mtel.denza.alfresco.servlet;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;

import at.mtel.denza.alfresco.scan.AppPropertyReader;

public abstract class AlfrescoWebScript {
	
	public abstract CloseableHttpResponse sendPost(String... params) throws Exception ;
	
	public abstract String parseResponse(CloseableHttpResponse response) throws IllegalStateException, IOException;
	
	protected String alfWebServiceURL = AppPropertyReader.getParameter("alfresco.url") + AppPropertyReader.getParameter("alfresco.service.api");
}
