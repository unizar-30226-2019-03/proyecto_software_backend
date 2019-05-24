/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Control del lanzamiento, ejecucion y debuggeo de la aplicacion de spring
 */

@SpringBootApplication
public class UnicastBackendApplication {

	// Funcion de control
	public static void main(String[] args) {
		SpringApplication.run(UnicastBackendApplication.class, args);
	}
}
