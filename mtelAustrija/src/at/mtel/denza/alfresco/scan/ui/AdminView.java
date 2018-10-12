package at.mtel.denza.alfresco.scan.ui;

import com.vaadin.annotations.Theme; 
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import at.mtel.denza.alfresco.jpa.Document;
import at.mtel.denza.alfresco.jpa.Metadata;
import at.mtel.denza.alfresco.jpa.User;
import at.mtel.denza.alfresco.scan.pojo.ListUtil;

@SuppressWarnings("serial")
@Theme("mtelaustrija")
public class AdminView extends VerticalLayout implements View {

	// layouti za gridove
	private VerticalLayout vl1 = new VerticalLayout();
	private VerticalLayout vl2 = new VerticalLayout();
	// slika
	private ThemeResource resource = new ThemeResource("img/3Dmtel_logo.jpg");
	private Image image = new Image();
	/**
	 * 
	 *  TODO - provjeriti da li je web service dostupan
	 */
		
	// lista svih klijentskih dokumenata
	private BeanItemContainer<Metadata> mContainer = new BeanItemContainer<Metadata>(Metadata.class, ListUtil.genericGetFromWebService("metadatas", new Metadata()));
	// lista svih tipova dokumenata
	private BeanItemContainer<Document> docContainer = new BeanItemContainer<Document>(Document.class, ListUtil.genericGetFromWebService("documents", new Document()));
	// lista svih korisnika
	private BeanItemContainer<User> uContainer = new BeanItemContainer<User>(User.class, ListUtil.genericGetFromWebService("users", new User()));

	public AdminView() {
		setUpLayout(vl1);
		image.setSource(resource);
		
		//logoff dugme
		mContainer.addNestedContainerBean("customer");
		mContainer.addNestedContainerBean("subscriber");
		mContainer.addNestedContainerBean("document");

		Grid gMetadata = new Grid();
		gMetadata.setContainerDataSource(mContainer);
		if(mContainer.size() > 0)
			gMetadata.setHeightByRows(mContainer.size());
		//gMetadata.removeColumn("nodeRef");
		//gMetadata.removeColumn("id");
		setUpGrid(gMetadata, "<h2>Korisnici i dokumenti</h2>");
		//gMetadata.getColumn("fileName").setExpandRatio(0);
		//gMetadata.getColumn("customerId").setExpandRatio(0);
		gMetadata.setWidth("830px");

		Grid gDoctype = new Grid();
		gDoctype.setContainerDataSource(docContainer);
		if(docContainer.size() > 0)
			gDoctype.setHeightByRows(docContainer.size());
		//gDoctype.setColumnOrder("id", "documentType");
		setUpGrid(gDoctype, "<h2>Tipovi dokumenata</h2>");
		gDoctype.setWidth("450px");
		
		
		Grid gUsers = new Grid();
		gUsers.setContainerDataSource(uContainer);
		if(uContainer.size() > 0)
			gUsers.setHeightByRows(uContainer.size());
		
		setUpGrid(gUsers, "<h2>Operateri</h2>");
		gUsers.setWidth("450px");
		
		Button logOffBtn = new CustomComponents().LogOffBtn();
		vl2.addComponent(logOffBtn);
		vl2.setComponentAlignment(logOffBtn, Alignment.TOP_RIGHT);

		TabSheet tabsheet = new TabSheet();

		// Create the first tab
		VerticalLayout tab1 = new VerticalLayout();
		tab1.addComponent(gMetadata);
		tab1.setMargin(true);

		tabsheet.addTab(tab1, "Skenirani dokumenti");

		// This tab gets its caption from the component caption
		VerticalLayout tab2 = new VerticalLayout();
		tab2.addComponent(gDoctype);
		tabsheet.addTab(tab2, "Tipovi dokumenata");
		tab2.setMargin(true);

		// This tab gets its caption from the component caption
		VerticalLayout tab3 = new VerticalLayout();
		tab3.addComponent(gUsers);
		tabsheet.addTab(tab3, "Operateri");
		tab3.setMargin(true);	

		tabsheet.setWidth("851px");
		vl2.setWidth("851px");
		this.addComponent(image);
		this.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		this.addComponent(vl2);
		this.setComponentAlignment(vl2, Alignment.MIDDLE_CENTER);
		this.addComponent(tabsheet);
		this.setComponentAlignment(tabsheet, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		UI.getCurrent().setContent(this);
	}

	private void setUpLayout(VerticalLayout vl) {
		vl.setSizeFull();
		//vl.setSpacing(true);
		vl.setMargin(true);

	}

	private void setUpGrid(Grid g, String caption) {
		g.setCaptionAsHtml(true);
		g.setCaption(caption);
		g.setImmediate(true);
		g.setEditorEnabled(true);
	}
}