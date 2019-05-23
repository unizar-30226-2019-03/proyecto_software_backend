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

import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.repository.rest.VoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;


/*
 * Permite serializar la entidad voto a su correspondiente fichero json
 */

@Component
public class VoteResourceAssembler implements ResourceAssembler<Vote, Resource<Vote>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Vote> toResource(Vote vote) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(vote,
                entityLinks.linkToSingleResource(VoteRepository.class, vote.getId()).withSelfRel());
    }
}