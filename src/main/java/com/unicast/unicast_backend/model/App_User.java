package com.unicast.unicast_backend.model;

import java.util.Set;

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
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class App_User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String nickname;
	
	private String email;
	
	private String photo;
	
	private String description;
	
	private boolean enabled;
	
	private byte[] password;
	
	@ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;
	
	@OneToMany(mappedBy = "user")
    Set<Notification_App_User> notifications;
    
    //TODO: Investigar los roles para ver como se hace.
    
    /*@OneToMany(mappedBy = "alumn")
    Set<Message> mesagges_a;
    
    @OneToMany(mappedBy = "teacher")
    Set<Message> messages_t;*/
	
	
	@JoinTable(
			  name = "app_user_subject", 
			  joinColumns = @JoinColumn(name = "fk_app_user"), 
			  inverseJoinColumns = @JoinColumn(name = "fk_subject"))
			Set<Notification> subjects;
	

}
