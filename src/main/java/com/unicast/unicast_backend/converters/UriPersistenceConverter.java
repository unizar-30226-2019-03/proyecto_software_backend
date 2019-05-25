/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.converters;

import java.net.URI;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*
 * Convertidor de atributos JPA
 * Se facilita una coleccion de funciones para poder realizar conversiones
 * entre la base de datos y los objetos en java los atributos de las entidades
 */

@Converter(autoApply = true)
public class UriPersistenceConverter implements AttributeConverter<URI, String> {

    /*
     * Permite la conversion de un atributo del objeto java a una columna de la
     * entidad correspondiente en la base de datos
     */
    @Override
    public String convertToDatabaseColumn(URI entityValue) {
        return (entityValue == null) ? null : entityValue.toString();
    }


    /*
     * Permite la conversion del valor del atributo del objeto java a su 
     * equivalente valor del atributo en la base de datos
     */
    @Override
    public URI convertToEntityAttribute(String databaseValue) {
        return (databaseValue != null && databaseValue.length() > 0 ? URI.create(databaseValue.trim()) : null);
    }
}
