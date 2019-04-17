package com.unicast.unicast_backend.controllers;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.repository.ReproductionListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;


@RepositoryRestController
public class ReproductionListController {

    private final ReproductionListRepository repo;

    @Autowired
    public ReproductionListController(ReproductionListRepository u){
        this.repo = u;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/reprodlist/ordenadas") 
    public @ResponseBody ResponseEntity<?> getAllReproductionsList() {
        // Guarda una lista con todas de todas las listas de reproduccion del usuario usuario 
        // ordenadas por nombre ascendente
        List<ReproductionList> reproductionsList = repo.findAll((Sort.by(Sort.Direction.ASC, "name")));

        // Devuelve el resultado 
        return ResponseEntity.ok(reproductionsList); 
    }

    

    @RequestMapping(method = RequestMethod.GET, value = "/reprodlist/hallarUna") 
    public ReproductionList getOneReproductionLIst(@RequestParam long oid) {

        // Guarda una lista con todas de todas las listas de reproduccion del usuario usuario
        List<ReproductionList> reproductionsList = repo.findAll((Sort.by(Sort.Direction.ASC, "name")));

        // Variable de control de lista encontrada 
        boolean encontrado = false;

        // Crear iterador que recorre la lista de reproducciones
        int i = 0;

        while (!encontrado && i < reproductionsList.size()){
            // Comparar si es la lista que buscamos
            if (reproductionsList.get(i).getId().equals(oid)){
                encontrado = true;
            }
            else {
                i++;
            }
        }
        if (encontrado){
            // Devuelve la lista de reproduccion
            return reproductionsList.get(i);
        }  
        else {
            // La lista con oid no existe
            return null;
        }
    }

}
