package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * The persistent class for the Subscriber database table.
 * 
 */
@Entity
@Table(name="subscribers")
@NamedQuery(name="Subscriber.findAll", query="SELECT s FROM Subscriber s")
@SequenceGenerator(name="subscribers_seq",sequenceName="subscribers_id_seq", allocationSize=1)
public class Subscriber implements Serializable {
	private static final long serialVersionUID = 1L; 

	@Id
	@GeneratedValue(generator="subscribers_seq")
	private Integer id;

	@Column(name="subscriber_id")
	private String subscriberId;

	private Long msisdn;
	
	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="customer_id")
    @JsonBackReference
	private Customer customer;

	//bi-directional many-to-one association to Metadata
	@OneToMany(mappedBy="subscriber", cascade = CascadeType.ALL)
	private List<Metadata> metadata;

	public Subscriber() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
		metadata.setSubscriber(this);

		return metadata;
	}

	public Metadata removeMetadata(Metadata metadata) {
		getMetadata().remove(metadata);
		metadata.setSubscriber(null);

		return metadata;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
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
		Subscriber other = (Subscriber) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;
		return true;
	}
	
	

}