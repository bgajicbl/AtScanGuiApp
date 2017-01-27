package at.mtel.denza.alfresco.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
@SequenceGenerator(name="users_seq",sequenceName="users_id_seq", allocationSize=1)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(generator="users_seq")
	private Integer id;

	private String password;

	private String status;

	private String type;

	private String username;

	public User() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String toString() {
		return getUsername() + "   " + getStatus() + "   " + getType();
	}

}