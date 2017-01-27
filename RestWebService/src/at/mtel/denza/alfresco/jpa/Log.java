package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the logs database table.
 * 
 */
@Entity
@Table(name="logs")
@NamedQuery(name="Log.findAll", query="SELECT l FROM Log l")
@SequenceGenerator(name="log_seq",sequenceName="logs_id_seq", allocationSize=1)
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(generator="log_seq")
	private Integer id;

	@Column(name="metadata_id")
	private Integer metadataId;

	private Timestamp time;

	private String user;

	public Log() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMetadataId() {
		return this.metadataId;
	}

	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}