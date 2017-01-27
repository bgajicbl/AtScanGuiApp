package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the documents database table.
 * 
 */
@Entity
@Table(name="documents")
@NamedQuery(name="Document.findAll", query="SELECT d FROM Document d")
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String document;

	//bi-directional many-to-one association to Metadata
	@OneToMany(mappedBy="document")
	private List<Metadata> metadata;

	public Document() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocument() {
		return this.document;
	}

	public void setDocument(String document) {
		this.document = document;
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
		metadata.setDocument(this);

		return metadata;
	}

	public Metadata removeMetadata(Metadata metadata) {
		getMetadata().remove(metadata);
		metadata.setDocument(null);

		return metadata;
	}

}