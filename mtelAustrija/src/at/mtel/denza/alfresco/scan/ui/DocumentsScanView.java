package at.mtel.denza.alfresco.scan.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import at.mtel.denza.alfresco.jpa.Customer;
import at.mtel.denza.alfresco.jpa.Document;
import at.mtel.denza.alfresco.jpa.Metadata;
import at.mtel.denza.alfresco.jpa.Subscriber;
import at.mtel.denza.alfresco.jpa.User;
import at.mtel.denza.alfresco.scan.MyUploadReceiver;
import at.mtel.denza.alfresco.scan.UtilClass;
import at.mtel.denza.alfresco.scan.pojo.ListUtil;

public class DocumentsScanView extends VerticalLayout implements View {

	private static final long serialVersionUID = -3141660789959884372L;

	private ThemeResource resource = new ThemeResource("img/3Dmtel_small.jpg");
	private Image image = new Image();
	private MyUploadReceiver mur = new MyUploadReceiver();
	private Integer documentID;
	private Customer c;
	private Subscriber s;
	private User upojo;
	private VerticalLayout vl = new VerticalLayout();
	ComboBox cbDocumentType;
	private SuggestingComboBox customersCB;
    ComboBox cbSubscribers = new ComboBox();
	List<Customer> korisniciLista = new ArrayList<>();
	BeanItemContainer<Customer> containerKorisnici = null;


	public DocumentsScanView() {
	}

	@Override
	public void enter(ViewChangeEvent event) {

		vl.removeAllComponents();
		image.setSource(resource);

		// link za administraciju
		Button b = new Button("Adminstracija");
		b.setStyleName(BaseTheme.BUTTON_LINK);
		b.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4184989937471431154L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(NavigatorUI.ADMINVIEW);
			}
		});

		User up = VaadinSession.getCurrent().getAttribute(User.class);
		if (up != null) {

			// log off dugme
			Button logOffBtn = new CustomComponents().LogOffBtn();
			vl.addComponent(logOffBtn);
			vl.setComponentAlignment(logOffBtn, Alignment.TOP_RIGHT);

			// username info
			vl.addComponent(new Label("ID korisnika:  " + up.getUsername()));
			if (up.getType().equalsIgnoreCase("A")) {
				vl.addComponent(b);
				vl.setComponentAlignment(b, Alignment.TOP_RIGHT);
			}
		} else {
			// uraditi log off
			new SessionHandler().logOff();
		}

		// lista dokumenata koji se mogu skenirati
		cbDocumentType = new ComboBox();
		List<Document> listaDoc = new ArrayList<>();
		listaDoc = ListUtil.genericGetFromWebService("documents", new Document());

		BeanItemContainer<Document> ctDocumentType = new BeanItemContainer<Document>(Document.class, listaDoc);
		setUpCombobox(cbDocumentType, "Tip dokumenta", ctDocumentType, "document");

		// lista korisnka za koje je moguce skenirati dokument
        final CustomerSuggestingContainer container = new CustomerSuggestingContainer();

        customersCB = new SuggestingComboBox("Klijent", "customerId");

		// korisnici = ListUtil.getAllCustomers();
		containerKorisnici = new BeanItemContainer<Customer>(Customer.class,
				korisniciLista);
		//setUpCombobox(cbKorisnik, "Klijent", containerKorisnici, "customerId");

		cbSubscribers = new ComboBox();

		final Upload upload = new Upload();
		upload.setCaption(null);
		upload.setButtonCaption("Dodaj");
		upload.setImmediate(true);
		upload.setReceiver(mur);
		upload.addSucceededListener(mur);
		upload.addFailedListener(mur);

		cbDocumentType.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -3187208539728251725L;

			public void valueChange(final ValueChangeEvent eventCbox) {
				Document d = (Document) eventCbox.getProperty().getValue();
				vl.removeComponent(customersCB);
				vl.removeComponent(upload);
				vl.removeComponent(cbSubscribers);
				
				//korisniciLista = ListUtil.genericGetFromWebService("customers", new Customer());
				containerKorisnici.addAll(korisniciLista);

				vl.addComponent(customersCB);
				if (c != null) {
					vl.addComponent(upload);
					vl.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
				}
				vl.setComponentAlignment(customersCB, Alignment.MIDDLE_CENTER);
				documentID = d.getId();
				mur.setType(d.getDocument());

				customersCB.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						vl.removeComponent(upload);
						vl.removeComponent(cbSubscribers);
		                container.setSelectedCustomer((Customer) event.getProperty().getValue());

						s = null;
						c = (Customer) event.getProperty().getValue();
						HashMap<String, String> params = new HashMap<>();
						params.put("id", c.getCustomerId());
						List<Subscriber> subscribers = c.getSubscribers();
						// System.out.println("****SIZE: "+subscribers.size());
						// List<Customer> lista =
						// ListUtil.genericGetFromWebService("customers/msisdn",
						// new Customer(), params);
						if (!subscribers.isEmpty()) {
							BeanItemContainer<Subscriber> containerSubscribers = new BeanItemContainer<Subscriber>(
									Subscriber.class, subscribers);

							setUpCombobox(cbSubscribers, "Subscriber", containerSubscribers, "subscriberId");
							// cbSubscribers.setNullSelectionAllowed(true);
							// cbSubscribers.setValue(cbSubscribers.getItemIds().iterator().next());
							vl.addComponent(cbSubscribers);
							vl.setComponentAlignment(cbSubscribers, Alignment.MIDDLE_CENTER);
						}
						mur.setId(String.valueOf(c.getCustomerId()));

						/*
						 * if(sub != null){
						 * mur.setId(String.valueOf(c.getCustomerId())+"-"+String.valueOf(sub.
						 * getSubscriberId()));
						 * 
						 * }
						 */
						vl.addComponent(upload);
						vl.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
					}
				});
				cbSubscribers.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						s = (Subscriber) event.getProperty().getValue();
					}
				});
			}
		});
        customersCB.setContainerDataSource(container);


		// kada se fajl uspjesno aploduje treba upisati u tabelu
		upload.addSucceededListener(new Upload.SucceededListener() {

			private static final long serialVersionUID = -5567214068515977578L;

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				ErrorMessage errorMessage = event.getUpload().getComponentError();
				if (errorMessage != null) {
					Notification.show("Greška! Fajl nije dodan na server.", Notification.Type.ERROR_MESSAGE);
					getUI().getNavigator().navigateTo(NavigatorUI.STARTVIEW);
				} else {

					// ostao da se definsise period!!!!!!!!!!!!!
					HashMap<String, String> params = new HashMap<>();
					if (s != null) {
						params.put("subscriberId", s.getSubscriberId());
					}
					params.put("customerId", c.getCustomerId());
					params.put("period", UtilClass.getDate().toString());
					params.put("nodeRef", mur.getOdgovor());
					params.put("documentType", documentID.toString());
					ListUtil.genericGetFromWebService("metadatas/insert", new Metadata(), params);
					Notification.show("Fajl dodan na server", mur.getFile().getName(),
							Notification.Type.TRAY_NOTIFICATION);
				}
			}
		});

		upload.addFailedListener(new Upload.FailedListener() {

			@Override
			public void uploadFailed(FailedEvent event) {
				Notification.show("Greška! Fajl nije dodan na server.", Notification.Type.ERROR_MESSAGE);
				getUI().getNavigator().navigateTo(NavigatorUI.STARTVIEW);

			}
		});

		vl.addComponent(cbDocumentType);
		vl.setComponentAlignment(cbDocumentType, Alignment.MIDDLE_CENTER);
		vl.setWidth("426px");

		this.addComponent(image);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		this.addComponent(vl);
		this.setComponentAlignment(vl, Alignment.MIDDLE_CENTER);
		this.setSpacing(true);
		this.setMargin(true);
		UI.getCurrent().setContent(this);
	}

	private void setUpCombobox(ComboBox cb, String caption, Container container, String captionProperty) {
		cb.setCaption(caption);
		cb.setContainerDataSource(container);
		cb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cb.setItemCaptionPropertyId(captionProperty);
		cb.setNullSelectionAllowed(false);
		cb.setScrollToSelectedItem(true);
		cb.setImmediate(true);
	}

	public User getUpojo() {
		return upojo;
	}

	public void setUpojo(User upojo) {
		this.upojo = upojo;
	}
}