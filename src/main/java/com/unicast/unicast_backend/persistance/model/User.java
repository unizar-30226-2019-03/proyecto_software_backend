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

import org.hibernate.annotations.Formula;
import org.springframework.data.rest.core.annotation.RestResource;

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
	@JsonIgnore
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
	@RestResource(exported = false)
	private Collection<UserIsNotified> notifications;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
    private Collection<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	@RestResource(exported = false)
	private Collection<Message> messagesAsReceiver;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	@RestResource(exported = false)
	private Collection<Message> messagesAsSender;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	private Collection<ReproductionList> reproductionLists;

	@OneToMany(mappedBy = "uploader")
	private Collection<Video> uploadedVideos;
	
	@ManyToMany(mappedBy = "followers")
	private Collection<Subject> subjects;

	@ManyToMany(mappedBy = "professors")
	private Collection<Subject> subjectsAsProfessor;

	@Formula("(select r.name from role r join role_app_user rau on r.id = rau.fk_role and id = rau.fk_app_user)")
	private String role;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "role_app_user",
		joinColumns = @JoinColumn(
			name = "fk_app_user", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_role", referencedColumnName = "id"))
	private Collection<Role> rolesAndPrivileges;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	private Collection<Display> displays;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	private Collection<Vote> votes;

	@ManyToOne
    @JoinColumn(name = "fk_degree")
	private Degree degree;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
