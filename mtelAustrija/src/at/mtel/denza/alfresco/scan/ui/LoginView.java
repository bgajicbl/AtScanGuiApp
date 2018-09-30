package at.mtel.denza.alfresco.scan.ui;

import java.util.HashMap;
import java.util.List;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import at.mtel.denza.alfresco.jpa.User;
import at.mtel.denza.alfresco.scan.AppPropertyReader;
import at.mtel.denza.alfresco.scan.pojo.ListUtil;

public class LoginView extends VerticalLayout implements View {

	private static final long serialVersionUID = 2905422194436166479L;
	private VerticalLayout vl = new VerticalLayout();
	private Image image = new Image();

	public LoginView() { 
	}

	@Override
	public void enter(ViewChangeEvent event) {

		vl.removeAllComponents();
		Notification.show("Dobrodosli u M:tel Austrija DMS");
		ThemeResource resource = new ThemeResource("img/3Dmtel_small.jpg");
		image.setSource(resource);
		final TextField tf = new TextField("Korisnicko ime: ");
		final PasswordField pf = new PasswordField("Lozinka: ");
		Button b = new Button("Prijava");

		b.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2959352603950846787L;

			@Override
			public void buttonClick(ClickEvent event) {
				HashMap<String, String> params = new HashMap<>();
				params.put("u", tf.getValue());
				params.put("p", pf.getValue());
				User u = new User();
				List<User> lista = null;
				try {
					lista = ListUtil.genericGetFromWebService("users/check", u, params);
				}catch(Exception e) {
					Notification.show("Greska u komunikaciji sa servisom za autentifikaciju.", Notification.Type.ERROR_MESSAGE);
					e.printStackTrace();
					return;
				}
				if (lista != null && lista.get(0) != null) {
					getSession().setAttribute(User.class, lista.get(0));
					getUI().getNavigator().navigateTo(NavigatorUI.STARTVIEW);
				}
				else
					Notification.show("Niste unijeli ispravno korisnicko ime ili lozinku.", Notification.Type.ERROR_MESSAGE);
			}

		});
		b.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		vl.setWidth("426px");
		addAndSetupComponents(vl, new Component[] { tf, pf, b });

		this.removeComponent(vl);
		this.addComponent(image);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		this.addComponent(vl);
		this.setComponentAlignment(vl, Alignment.MIDDLE_CENTER);

		UI.getCurrent().setContent(this);
	}

	private void addAndSetupComponents(VerticalLayout vl, Component... coms) {
		for (Component c : coms) {
			vl.addComponent(c);
			vl.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
		}
	}
}