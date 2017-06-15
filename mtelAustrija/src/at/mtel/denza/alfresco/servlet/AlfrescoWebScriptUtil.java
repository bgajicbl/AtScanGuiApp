package at.mtel.denza.alfresco.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AlfrescoWebScriptUtil {
	
	public static String getTicket() throws IOException {
		URL url = new URL(AlfrescoWebScriptUtil.getAlfrescoIPAddress()+"alfresco/service/api/login?u=admin&pw=admin");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuffer sb = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();
		return sb.substring(sb.indexOf("<ticket>") + 8, sb.indexOf("</ticket>"));
	}
	
	public static String getAlfrescoIPAddress() {
		return alfrescoIPAddress;
	}

	private static String alfrescoIPAddress = "http://127.0.0.1:8080/";
}
