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

import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.repository.rest.UserIsNotifiedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/*
 * Permite serializar la entidad de notificaciones de usuarios a su 
 * correspondiente json
 */

@Component
public class UserIsNotifiedResourceAssembler implements ResourceAssembler<UserIsNotified, Resource<UserIsNotified>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<UserIsNotified> toResource(UserIsNotified userIsNotified) {
        return new Resource<>(userIsNotified,
                entityLinks.linkToSingleResource(UserIsNotifiedRepository.class, userIsNotified.getId()).withSelfRel());
    }
}