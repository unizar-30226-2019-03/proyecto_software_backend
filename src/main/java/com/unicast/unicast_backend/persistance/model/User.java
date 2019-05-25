/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

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

/*
 * Entidad de los usuarios
 */

@Data
@Entity
@Table(name = "app_user")
public class User {
	
	// Identificador del usuario
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	// Nombre de usuario del usuario
	private String username;

	// Nombre real del usuario
	private String name;

	// Apellidos reales del usuario
	private String surnames;
	
	// Correo del usuario
	@Email
	@JsonIgnore
	private String email;
	
	// Foto de perfil del usuario
	@Column(name = "photo_path")
	private URI photo;
	
	// Descripcion del usuario
	private String description;
	
	// Usuario habilitado o no
	@JsonIgnore
	private boolean enabled;
	
	// Contrasenya del usuario
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	// Relacion N:1 con universidad
	@ManyToOne
	@JoinColumn(name = "fk_university")
	// Universidad en la que el usuario esta matriculado
    private University university;
	
	// Relacion 1:N con notificaciones recibidas
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	// Coleccion de notificaciones recibidas
	private Collection<UserIsNotified> notifications;
	
	// Relacion 1:N con comentario
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	// Coleccion de comentarios hechos por el usuario
    private Collection<Comment> comments;
	
	// Relacion 1:N con mensaje
	@JsonIgnore
	@OneToMany(mappedBy = "receiver")
	@RestResource(exported = false)
	// Coleccion de mensajes que el usuario ha mandado
	private Collection<Message> messagesAsReceiver;
	
	// Relacion 1:N con mensaje
	@JsonIgnore
	@OneToMany(mappedBy = "sender")
	@RestResource(exported = false)
	// Coleccion de mensajes que el usuario ha recibido
	private Collection<Message> messagesAsSender;

	// Relacion 1:N con lista de reproduccion
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	// Coleccion de listas de reproduccion de un usuario
	private Collection<ReproductionList> reproductionLists;

	// Relacion 1:N con video
	@OneToMany(mappedBy = "uploader")
	// Coleccion de videos subidos por el usuario
	private Collection<Video> uploadedVideos;
	
	// Relacion N:1 con asignatura
	@ManyToMany(mappedBy = "followers")
	// Coleccion de asignaturas que estudia el usuario
	private Collection<Subject> subjects;

	// Relacion N:1 con asignaturas
	@ManyToMany(mappedBy = "professors")
	// Coleccion de asignaturas que imparte un usuario profesor
	private Collection<Subject> subjectsAsProfessor;

	// Consulta SQL para extraer el rol de un usuario
	@Formula("(select r.name from role r join role_app_user rau on r.id = rau.fk_role and id = rau.fk_app_user)")
	private String role;

	// Relacion N:M con usuarios y roles
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "role_app_user",
		joinColumns = @JoinColumn(
			name = "fk_app_user", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_role", referencedColumnName = "id"))
	// Coleccion de roles asociados a los privilegios de un usuario
	private Collection<Role> rolesAndPrivileges;

	// Relacion 1:N con display
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	// Coleccion de displays hechos por un usuario
	private Collection<Display> displays;
	
	// Relacion 1:N con votos
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	@RestResource(exported = false)
	// Coleccion de votos hechos por un usuario
	private Collection<Vote> votes;

	// Relacion N:1 con carrera
	@ManyToOne
	@JoinColumn(name = "fk_degree")
	// Carrera que cursa el usuario
	private Degree degree;

	/*
	 * Devuelve si dos usuarios son identicos o no
	 */
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

	/*
	 * Metodo no utilizado 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
