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

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/*
 * Permite modelar la entidad carrera a la que pertenecen los estudiantes 
 * presente en la base de datos
 */

@Data
@Entity
@Table(name = "degree")
public class Degree {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Identificador de la carrera
    private Long id;

	// Nombre de la carrera
    private String name;

	// Relacion 1:N con usuario
	@OneToMany(mappedBy = "degree")
	// Coleccion de usuarios que estudian esa carrera
    private Collection<User> users;

	// Relacion con las universidades donde se imparte la carrera
    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "degree_university",
		joinColumns = @JoinColumn(
			name = "fk_degree", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_university", referencedColumnName = "id"))
				
	// Coleccion de universidades donde se imparte esa carrera
    private Collection<University> universities;
}
