package at.mtel.denza.alfresco.scan.ui;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import at.mtel.denza.alfresco.scan.ui.NavigatorUI;

public class CustomComponents {
	Button b = new Button();

	public Button LogOffBtn(){
		b.setCaption("Log off");
		b.setStyleName(BaseTheme.BUTTON_LINK);
		
		b.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6424776760885497778L;

			@Override
			public void buttonClick(ClickEvent event) {
				new SessionHandler().logOff();
			}
		});

		return b;
		
	}
	
}
