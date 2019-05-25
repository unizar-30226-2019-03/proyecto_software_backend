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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/*
 * Entidad universidad 
 */

@Data
@Entity
@Table(name = "university")
public class University {
    
    // Identificador de la universidad
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la universidad
    private String name;

    // Foto de la universidad
    @Column(name = "uni_photo")
	private URI photo;

    // Relacion 1:N con usuarios
    @OneToMany(mappedBy = "university")
    // Coleccion de alumnos matriculados en la universidad
    private Collection<User> users;
    
    // Relacion 1:N con asignaturas
    @JsonIgnore
    @OneToMany(mappedBy = "university")
    // Coleccion de asignaturas impartidas en la universidad
    private Collection<Subject> subjects;

    // Relacion N:M con carrera
    @ManyToMany(mappedBy = "universities")
    // Coleccion de carreras impartidas en la universidad
    private Collection<Degree> degrees;

}