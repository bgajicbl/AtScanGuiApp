package at.mtel.denza.alfresco.scan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinService;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import at.mtel.denza.alfresco.servlet.AlfrescoCreateFolder;
import at.mtel.denza.alfresco.servlet.AlfrescoUploadFile;

public class MyUploadReceiver implements Receiver, SucceededListener, FailedListener {
	
	private static final long serialVersionUID = -4297864645900110429L; 
	private File file = new File("");
	private String id;
	private String type;

	private String mtelAustrijaNodeRef = AppPropertyReader.getParameter("mtel.node.ref");
	
	private String odgovor;

	public MyUploadReceiver() {
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		FileOutputStream fos = null;
		try {
			// ovdje dodaj period za ugovor i slicno uzimaj mjesec i godinu   
			StringBuilder sb = new StringBuilder(VaadinService.getCurrent().getBaseDirectory()+"/VAADIN/"+getType()+"_");
			// dodaj datum
			sb.append(UtilClass.getDate()).append('_');
			// dodaj vrijeme
			sb.append(UtilClass.getTime());
			// dodaj ekstenziju
			sb.append(filename.substring(filename.indexOf('.')));
			// otvori stream
			file = new File(sb.toString());
			fos = new FileOutputStream(file);
		}
		catch (Exception e) {
			Notification.show("Could not open file", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		try {
			// kreiraj folder ako vec ne postoji
			new AlfrescoCreateFolder().sendPost(id, id, "Folder za klijenta "+id, "cm:folder", mtelAustrijaNodeRef);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fos;
	}
	
	public void uploadSucceeded(SucceededEvent event) {
		CloseableHttpResponse chc;
		try {
			// upload fajla kao odgovor se vraca nodeRef
			chc = new AlfrescoUploadFile().sendPost(id, mtelAustrijaNodeRef, "Fajl", file.getPath());
			String odg = new AlfrescoUploadFile().parseResponse(chc);
			//System.out.println("****ODG: "+odg);
			if(odg == null) {
				event.getUpload().setComponentError(new ErrorMessage() {
					
					@Override
					public String getFormattedHtmlMessage() {
						return "Fajl nije dodan u Alfresco!";
					}
					
					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.CRITICAL;
					}
				});
			}else if(odg.startsWith("work")) {
				odg = odg.substring(odg.lastIndexOf('/')+1);
			}
			setOdgovor(odg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void uploadFailed(FailedEvent event) {
		System.out.println("****UPLOAD FAILED: " + file.getName());
		
	}
	
	public String getOdgovor() {
		return odgovor;
	}

	public void setOdgovor(String odgovor) {
		this.odgovor = odgovor;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

}