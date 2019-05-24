/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

/*
 * Repositorio JPA para gestionar la contencion de videos en
 * las listas de reproduccion de los usuarios
 */

package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Contains;
import com.unicast.unicast_backend.persistance.model.ContainsKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainsRepository extends JpaRepository<Contains,ContainsKey> {
}