package at.mtel.denza.alfresco.scan.ui;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import at.mtel.denza.alfresco.jpa.User;

public class SessionHandler {

	public void logOff(){
		UI.getCurrent().getPage().setLocation("");	
		VaadinSession.getCurrent().close();
	}
}
