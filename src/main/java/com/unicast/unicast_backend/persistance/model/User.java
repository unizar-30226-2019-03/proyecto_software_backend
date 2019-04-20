package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String username;
	
	@Email
	private String email;
	
	@Column(name = "photo_path")
	private URI photo;
	
	private String description;
	
	private boolean enabled;
	
	private String password;

	@JsonIgnore
	public Collection<Vote> getVotes(){
		return this.votes;
	}
	
	@ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;
	
	@OneToMany(mappedBy = "user")
	private Collection<UserIsNotified> notifications;
	
	@OneToMany(mappedBy = "user")
    private Collection<Comment> comments;
	
	@OneToMany(mappedBy = "receiver")
	private Collection<Message> messagesAsReceiver;
	
	@OneToMany(mappedBy = "sender")
	private Collection<Message> messagesAsSender;

	@OneToMany(mappedBy = "user")
	private Collection<ReproductionList> reproductionLists;
	
	// Vale tanto para alumnos como para profesores, cambiara segun el rol que tenga este usuario
	@ManyToMany
	@JoinTable(
		name = "app_user_subject", 
		joinColumns = @JoinColumn(name = "fk_app_user"), 
		inverseJoinColumns = @JoinColumn(name = "fk_subject"))
	private Collection<Subject> subjects;

	@ManyToMany
	@JoinTable(
		name = "role_app_user",
		joinColumns = @JoinColumn(
			name = "fk_app_user", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_role", referencedColumnName = "id"))
	private Collection<Role> roles;

	@OneToMany(mappedBy = "user")
	private Collection<Display> collectionDisplays;
	
	@OneToMany(mappedBy = "user")
    private Collection<Vote> votes;

}
