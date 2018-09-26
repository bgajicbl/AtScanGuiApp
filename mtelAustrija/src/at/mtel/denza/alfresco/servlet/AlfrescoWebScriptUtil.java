package at.mtel.denza.alfresco.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import at.mtel.denza.alfresco.scan.AppPropertyReader;

public class AlfrescoWebScriptUtil {
	
	public static String getTicket() throws Exception {
		String alfrescoIp = getAlfrescoIPAddress();
		String alfrescoLoginService = AppPropertyReader.getParameter("alfresco.service.login");
		String alfrescoUser = AppPropertyReader.getParameter("alfresco.user");
		String alfrescoPass = AppPropertyReader.getParameter("alfresco.pass");
		
		URL url = new URL(alfrescoIp+alfrescoLoginService+"?u="+alfrescoUser+"&pw="+alfrescoPass);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuffer sb = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();
		return sb.substring(sb.indexOf("<ticket>") + 8, sb.indexOf("</ticket>"));
	}
	
	public static String getAlfrescoIPAddress() {
		return AppPropertyReader.getParameter("alfresco.url");
	}
	
}
