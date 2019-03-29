package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;
import java.net.URI;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String username;
	
	@Email
	private String email;
	
	private URI photo;
	
	private String description;
	
	private boolean enabled;
	
	private String password;
	
	@ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;
	
	@OneToMany(mappedBy = "user")
    private Collection<UserIsNotified> notifications;
	
	@ManyToMany
	@JoinTable(
		name = "role_app_user",
		joinColumns = @JoinColumn(
			name = "fk_app_user", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_role", referencedColumnName = "id"))

	private Collection<Role> roles;

    //TODO: Investigar los roles para ver como se hace.
    
    /*@OneToMany(mappedBy = "alumn")
    Set<Message> mesagges_a;
    
    @OneToMany(mappedBy = "teacher")
    Set<Message> messages_t;*/
	
	/*@ManyToMany
	@JoinTable(
		name = "app_user_subject", 
		joinColumns = @JoinColumn(name = "fk_app_user"), 
		inverseJoinColumns = @JoinColumn(name = "fk_subject"))
	Set<Subject> subjects;*/

}
