package at.mtel.denza.alfresco.scan.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme("mtelaustrija")
public class StartUI extends UI {

	@WebServlet(value = {"/gui/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = StartUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		nui.init(request);
		nui.navigator.navigateTo(NavigatorUI.LOGINVIEW);		
	}
	
	public NavigatorUI getNavigatorUI() {
		return nui;
	}
	
	NavigatorUI nui = new NavigatorUI(this);
}