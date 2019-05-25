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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/*
 * Clase adicional para modelar la relacion N:M entre videos y listas de reproduccion
 */

@Data
@Embeddable
public class ContainsKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Clave del video
	@Column(name = "fk_video")
    private Long videoId;
    
    // Clave de la lista
	@Column(name = "fk_list")
    private Long listId;
	
}