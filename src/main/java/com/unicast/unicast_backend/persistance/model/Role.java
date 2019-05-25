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
import javax.persistence.Table;

import lombok.Data;

/*
 * Roles de los usuarios
 */

@Data
@Entity
@Table(name = "role")
public class Role {

    // Identificador del rol 
    @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nombre del rol
    private String name;

    // Relacion N:1 con usuairio
    @ManyToMany(mappedBy = "rolesAndPrivileges")
    // Coleccionde privilegios de un usuario
    private Collection<User> users;

    // Relacion N:M con privilegios
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_privilege", 
        joinColumns = @JoinColumn(
          name = "fk_role", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "fk_privilege", referencedColumnName = "id"))

    // Coleccion de privilegios de un rol
    private Collection<Privilege> privileges;  
}