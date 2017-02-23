package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.mtel.denza.alfresco.json.JsonDateDeserializer;
import at.mtel.denza.alfresco.json.JsonDateSerializer;


/**
 * The persistent class for the metadata database table.
 * 
 */
@Entity
@Table(name="metadata")
@NamedQuery(name="Metadata.findAll", query="SELECT m FROM Metadata m")
@SequenceGenerator(name="metadata_seq",sequenceName="metadata_id_seq", allocationSize=1)
public class Metadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="metadata_seq")
	private Integer id;

	private String noderef;

	@Temporal(TemporalType.DATE)
	private Date period;

	//bi-directional many-to-one association to Customer
	@ManyToOne()
	@JoinColumn(name="customers_id")
	private Customer customer;
	
	//bi-directional many-to-one association to Subscriber
	@ManyToOne()
	@JoinColumn(name="subscriber_id")
	private Subscriber subscriber; 

	//bi-directional many-to-one association to Document
	@ManyToOne
	@JoinColumn(name="documents_id")
	private Document document;

	public Metadata() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNoderef() {
		return this.noderef;
	}

	public void setNoderef(String noderef) {
		this.noderef = noderef;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getPeriod() {
		return this.period;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setPeriod(Date period) {
		this.period = period;
	}

	@JsonIgnoreProperties({"id", "subscribers"})
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonIgnoreProperties({"id"})
	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@JsonIgnoreProperties({"id"})
	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
	
}