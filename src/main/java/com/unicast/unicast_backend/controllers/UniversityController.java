package com.unicast.unicast_backend.controllers;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.repository.UniversityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class UniversityController {

    private final UniversityRepository repo;

    @Autowired
    public UniversityController(UniversityRepository u){
        this.repo = u;
    }

    @GetMapping(value = "/universities/ordenadas") 
    public @ResponseBody ResponseEntity<?> getUniversities() {
        List<University> producers = repo.findAll((Sort.by(Sort.Direction.ASC, "name")));

        //
        // do some intermediate processing, logging, etc. with the producers
        //

       /* Resources<String> resources = new Resources<String>(producers); 

        resources.add(linkTo(methodOn(ScannerController.class).getProducers()).withSelfRel()); 

        // add other links as needed*/

        return ResponseEntity.ok(producers); 
    }

}

