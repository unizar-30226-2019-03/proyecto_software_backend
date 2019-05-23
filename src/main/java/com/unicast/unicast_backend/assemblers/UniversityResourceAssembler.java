/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.repository.rest.UniversityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/*
 * Permite serializar la entidad universidad a su correspondiente fichero json por medio de un link
 */

@Component
public class UniversityResourceAssembler implements ResourceAssembler<University, Resource<University>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<University> toResource(University university) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(university,
                entityLinks.linkToSingleResource(UniversityRepository.class, university.getId()).withSelfRel());
    }
}