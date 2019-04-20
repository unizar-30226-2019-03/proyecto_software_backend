package com.unicast.unicast_backend.controllers;

import java.io.IOException;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.repository.ReproductionListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class ReproductionListController {

    private final ReproductionListRepository repo;

    @Autowired
    public ReproductionListController(ReproductionListRepository u) {
        this.repo = u;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reprodlist/ordenadas")
    public @ResponseBody ResponseEntity<?> getAllReproductionsList() {
        // Guarda una lista con todas de todas las listas de reproduccion del usuario
        // ordenadas por nombre ascendente
        List<ReproductionList> reproductionsList = repo.findAll((Sort.by(Sort.Direction.ASC, "name")));

        // Devuelve el resultado
        return ResponseEntity.ok(reproductionsList);
    }

    @RequestMapping(value = "/reprodlist/anyadir", method = RequestMethod.POST)
    public void addNewReproductionList(@RequestParam ReproductionList repList)
            throws IllegalStateException, IOException {
        // Guarda una lista con todas de todas las listas de reproduccion del usuario
        List<ReproductionList> reproductionsList = repo.findAll();
        try {
            // Incorporar la nueva lista al conjunto de listas del usuario
            // ¿ Se puede hacer directamente ?
            int indice = getOneReproductionLIst(repList.getId());
            if (indice == -1) {
                // Borra la lista deseada de la lista
                reproductionsList.remove(indice);
            }
        } catch (Exception e) {
            // Mostrado por pantalla de la excepcion capturada
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/reprodlist/borrar", method = RequestMethod.POST)
    public void deleteReproductionList(@RequestParam ReproductionList repList) throws Exception {
        // Guarda una lista con todas de todas las listas de reproduccion del usuario
        List<ReproductionList> reproductionsList = repo.findAll();
        try {
            // Incorporar la nueva lista al conjunto de listas del usuario
            // ¿ Se puede hacer directamente ?
            reproductionsList.add(repList);
        } catch (Exception e) {
            // Mostrado por pantalla de la excepcion capturada
            e.printStackTrace();
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/reprodlist/hallarUna")
    public int getOneReproductionLIst(@RequestParam long oid) {

        // Guarda una lista con todas de todas las listas de reproduccion del usuario
        // usuario
        List<ReproductionList> reproductionsList = repo.findAll((Sort.by(Sort.Direction.ASC, "name")));

        // Variable de control de lista encontrada
        boolean encontrado = false;

        // Crear iterador que recorre la lista de reproducciones
        int i = 0;

        while (!encontrado && i < reproductionsList.size()) {
            // Comparar si es la lista que buscamos
            if (reproductionsList.get(i).getId().equals(oid)) {
                encontrado = true;
            } else {
                i++;
            }
        }
        if (encontrado) {
            // Devuelve la lista de reproduccion
            return i;
        } else {
            // La lista con oid no existe
            return -1;
        }
    }

}
