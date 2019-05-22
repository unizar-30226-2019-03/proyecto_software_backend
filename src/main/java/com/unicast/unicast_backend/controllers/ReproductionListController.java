/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.controllers;

import java.net.URI;

import com.unicast.unicast_backend.exceptions.NotOwnerReproductionList;
import com.unicast.unicast_backend.exceptions.FavouriteListException;
import com.unicast.unicast_backend.assemblers.ReproductionListAssembler;
import com.unicast.unicast_backend.persistance.model.Contains;
import com.unicast.unicast_backend.persistance.model.ContainsKey;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.ContainsRepository;
import com.unicast.unicast_backend.persistance.repository.rest.ReproductionListRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador de las listas de reproduccion
 */

@RestController
public class ReproductionListController {

    // Repositorio de listas de reproduccion
    private final ReproductionListRepository reproductionListRepository;

    // Repositorio de videos
    private final VideoRepository videoRepository;

    // Repositorio ensamblador
    private final ReproductionListAssembler reproductionListAssembler;

    // Repositorio de relaciones entre video y lista de reproduccion
    private final ContainsRepository containsRepository;


    /*
     * Constructor del Contorlador REST
     */
    @Autowired
    public ReproductionListController(ReproductionListRepository u, ReproductionListAssembler assembler,
            VideoRepository videoRepository, ContainsRepository containsRepository) {
        this.reproductionListRepository = u;
        this.reproductionListAssembler = assembler;
        this.videoRepository = videoRepository;
        this.containsRepository = containsRepository;
    }

    /*
     * Permite anyadir una nueva lista de reproduccion de videos
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param name: nombre de la lista de reproduccion
     */
    @PostMapping(value = "/api/reproductionLists/add", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("name") String name) throws Exception {

        // Obtencion de los datos del usuario loggeado
        User user = userAuth.getUser();

        // Creacion de la nueva lista de reproduccion y colocacion de atributos
        ReproductionList reproList = new ReproductionList();
        reproList.setUser(user);
        reproList.setName(name);

        // Actualizar los cambios en el repositorio
        reproductionListRepository.save(reproList);

        // Respuesta de la operacion
        Resource<ReproductionList> resourceReproList = reproductionListAssembler.toResource(reproList);
        return ResponseEntity.created(new URI(resourceReproList.getId().expand().getHref())).body(resourceReproList);
    }


    /*
     * Permite borrr una lista de reproduccion de videos
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param reproListId: identificador de la lista a borrar
     */
    @DeleteMapping(value = "/api/reproductionLists/{id}")
    public ResponseEntity<?> deleteReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @PathVariable(name = "id", required = true) Long reproListId) 
            throws FavouriteListException, NotOwnerReproductionList {

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Buscar la lista por su id en el repositorio 
        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        // Comprobacion de que el usuario es el propietario
        if (reproList.getUser().getId() != user.getId()) {
            throw new NotOwnerReproductionList("El usuario no es propietario de la lista de reproduccion a borrar");
        }

        // Comprobar que la lista no pertenece a favoritos
        if (reproList.getName().equals("Favoritos")) {
            throw new FavouriteListException("La lista a borrar esta marcada como favoritos");
        }

        // Borrado y envio de respuesta de operacion
        reproductionListRepository.delete(reproList);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Permite anyadir un video a una lista de reproduccion de videos
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param reproListId: identificador de la lista a la que se va a anyadir el video
     *  @param videoId: identificador del video a anyadir
     */
    @PostMapping(value = "/api/reproductionLists/addVideo", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> anyadirVideoReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("repro_list_id") Long reproListId, @RequestParam("video_id") Long videoId) throws Exception {
        
        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Buscar el video en el repositorio por id y obtener la lista de reproduccion por su id
        Video video = videoRepository.findById(videoId).get();
        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        // Comprobar que el usuario es el propietario de la lista
        if (reproList.getUser().getId() != user.getId()) {
            throw new NotOwnerReproductionList("El usuario no es propietario de la lista de reproduccion a borrar");
        }

        // Crear el contains Key
        ContainsKey ck = new ContainsKey();
        ck.setListId(reproList.getId());
        ck.setVideoId(video.getId());

        // Crear contains
        Contains c = new Contains();

        // Asignacion de atributos 
        c.setId(ck);
        c.setList(reproList);
        c.setVideo(video);
        c.setPosition(reproList.getVideoList().size() + 1);

        // Guardar cambios en el repositorio y enviar respuesta
        containsRepository.save(c);
        return ResponseEntity.ok().build();
    }


    /*
     * Permite borrar un video de lista de reproduccion de videos
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param reproListId: identificador de la lista a la que se va a borrar el video
     *  @param videoId: identificador del video a borrar
     */
    @DeleteMapping(value = "/api/reproductionLists/deleteVideo", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> deleteReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("repro_list_id") Long reproListId, @RequestParam("video_id") Long videoId) throws Exception {

        // Obtencion de los datos del usuario loggeado
        User user = userAuth.getUser();

        // Buscar la lista en el repositorio por su id
        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        // Comprobar que es propietario
        if (reproList.getUser().getId() != user.getId()) {
            throw new NotOwnerReproductionList("El usuario no es propietario de la lista de reproduccion a borrar");
        }

        // Crear la nueva relacion 
        ContainsKey ck = new ContainsKey();
        ck.setListId(reproListId);
        ck.setVideoId(videoId);

        // Borrado del video de la lista y envio de respuesta
        containsRepository.deleteById(ck);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}