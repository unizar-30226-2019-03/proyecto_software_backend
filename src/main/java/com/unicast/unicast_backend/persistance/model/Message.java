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

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/*
 * Entidad mensaje en la base de datos
 */

@Data
@Entity
@Table(name = "message")
public class Message {
    
    // Identificador del mensaje
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Texto del mensaje enviado o recibido
    private String text;
    
    // Instante de envio y recepcion del mensaje
    private Timestamp timestamp;
    
    // Relacion N:1 con usuario
    @ManyToOne
    @JoinColumn(name = "fk_receiver")
    // Usuario receptor del mensaje
    private User receiver;
    
    // Relacion N:1 con usuario
    @ManyToOne
    @JoinColumn(name = "fk_sender")
    // Usuario remitente del mensaje
    private User sender;
    
}
