package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the customers database table.
 * 
 */
@Entity
@Table(name="customers")
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
@SequenceGenerator(name="customer_seq",sequenceName="customers_id_seq", allocationSize=1)
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L; 

	@Id
	@GeneratedValue(generator="customer_seq")
	private Integer id;

	@Column(name="customer_id")
	private String customerId;

	private Long msisdn;
	
	//bi-directional many-to-one association to Subscriber
	@OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Subscriber> subscribers;

	//bi-directional many-to-one association to Metadata
	@OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
	private List<Metadata> metadata; 

	public Customer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@JsonIgnore(true)
	public Long getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}

	@JsonIgnore(true)
	public List<Metadata> getMetadata() {
		return this.metadata;
	}

	public void setMetadata(List<Metadata> metadata) {
		this.metadata = metadata;
	}

	public Metadata addMetadata(Metadata metadata) {
		getMetadata().add(metadata);
		metadata.setCustomer(this);

		return metadata;
	}

	public Metadata removeMetadata(Metadata metadata) {
		getMetadata().remove(metadata);
		metadata.setCustomer(null);

		return metadata;
	}
	
	
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	public Subscriber addSubscribers(Subscriber subscriber) {
		getSubscribers().add(subscriber);
		subscriber.setCustomer(this);

		return subscriber;
	}
	
	public Subscriber removeSubscriber(Subscriber subscriber) {
		getSubscribers().remove(subscriber);
		subscriber.setCustomer(null);

		return subscriber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}