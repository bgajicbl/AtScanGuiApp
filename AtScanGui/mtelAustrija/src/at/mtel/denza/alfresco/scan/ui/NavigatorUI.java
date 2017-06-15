package at.mtel.denza.alfresco.scan.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class NavigatorUI extends UI {
	
	private static final long serialVersionUID = -7620695539152627189L;
	protected Navigator navigator;
	protected static final String STARTVIEW = "DocumentsScanView";
	protected static final String ADMINVIEW = "AdminView";
	protected static final String LOGINVIEW = "LoginView";

	public NavigatorUI(UI ui) {
		navigator = new Navigator(ui, this);
	}
	
	@Override
	protected void init(VaadinRequest request) {
		// Create and register the views
		navigator.addView(LOGINVIEW, LoginView.class);
		navigator.addView(STARTVIEW, DocumentsScanView.class);
		navigator.addView(ADMINVIEW, AdminView.class);
	}
	
	protected void logOff(){
		
	}
	
}