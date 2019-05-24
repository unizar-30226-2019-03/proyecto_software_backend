/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Privilege;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repositorio para gestionar los privilegios de los usuarios
 */

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    /*
     * Permite devolver un privilegio buscandolo en el repositorio por su nombre
     */
    Privilege findByName(String name);

}