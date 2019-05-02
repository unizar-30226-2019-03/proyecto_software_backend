package com.unicast.unicast_backend.controllers;

import java.util.Collection;

import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.ReproductionListRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RepositoryRestController
public class ReproductionListController {

    private final ReproductionListRepository repo;

    @Autowired
    public ReproductionListController(ReproductionListRepository u) {
        this.repo = u;
    }

    
    @GetMapping(value = "api/reproductionList/mostrar", produces = "application/json")
    public Collection<ReproductionList> getUserReproducctionLists(User user)throws Exception {
         // Obtencion de la coleccion de listas de reproduccion del usuario loggeado
         Collection<ReproductionList> reproductionLists = user.getReproductionLists();

         // Retorno de las listas
         return reproductionLists;
    }

    @PostMapping(value = "/api/reproductionList/updateAdd", produces = "application/json")
    public void addReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
                                       @RequestParam ReproductionList newUserList)throws Exception {

        // Obtencion de los datos del usuario loggeado
        User user = userAuth.getUser();
        
        // Obtencion de la coleccion de listas de reproduccion del usuario loggeado
        Collection<ReproductionList> reproductionLists = getUserReproducctionLists(user);

        //Comprobar si existe la lista en las que ya tiene el ususario
        try {
            // Comprobar la existencia de esa lista
            reproductionLists.contains(newUserList);

            // La lista no existe y se debe añadir
            reproductionLists.add(newUserList);

            // Asignar la nueva coleccion de listas al usuario
            user.setReproductionLists(reproductionLists);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @PostMapping(value = "/api/reproductionList/updateRemove", produces = "application/json")
    public void deleteReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
                                       @RequestParam ReproductionList newUserList)throws Exception {

       // Obtencion de los datos del usuario loggeado
       User user = userAuth.getUser();
        
        // Obtencion de la coleccion de listas de reproduccion del usuario loggeado
        Collection<ReproductionList> reproductionLists = getUserReproducctionLists(user);

        //Comprobar si existe la lista en las que ya tiene el ususario
        try {
            // Comprobar la existencia de esa lista
            reproductionLists.contains(newUserList);

            // La lista no existe y se debe añadir
            reproductionLists.remove(newUserList);

            // Asignar la nueva coleccion de listas al usuario
            user.setReproductionLists(reproductionLists);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}