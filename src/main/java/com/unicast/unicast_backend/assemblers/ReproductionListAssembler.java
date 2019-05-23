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

import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.repository.rest.ReproductionListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/*
 * Permite serializar la entidad lista de reproduccion de video a su
 * fichero json mediante un link
 */

@Component
public class ReproductionListAssembler implements ResourceAssembler<ReproductionList, Resource<ReproductionList>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<ReproductionList> toResource(ReproductionList reproList) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(reproList,
                entityLinks.linkToSingleResource(ReproductionListRepository.class, reproList.getId()).withSelfRel());
    }
}