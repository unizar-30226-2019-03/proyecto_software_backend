package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String username;

	private String name;

	private String surnames;
	
	@Email
	private String email;
	
	@Column(name = "photo_path")
	private URI photo;
	
	private String description;
	
	@JsonIgnore
	private boolean enabled;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Collection<UserIsNotified> notifications;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
    private Collection<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	private Collection<Message> messagesAsReceiver;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	private Collection<Message> messagesAsSender;

	@OneToMany(mappedBy = "user")
	private Collection<ReproductionList> reproductionLists;

	@OneToMany(mappedBy = "uploader")
	private Collection<Video> uploadedVideos;
	
	// Vale tanto para alumnos como para profesores, cambiara segun el rol que tenga este usuario
	@ManyToMany(mappedBy = "users")
	private Collection<Subject> subjects;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "role_app_user",
		joinColumns = @JoinColumn(
			name = "fk_app_user", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_role", referencedColumnName = "id"))
	private Collection<Role> roles;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Collection<Display> displays;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Collection<Vote> votes;

	@ManyToOne
    @JoinColumn(name = "fk_degree")
    private Degree degree;
	
}
