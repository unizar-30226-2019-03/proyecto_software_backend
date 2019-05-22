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

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;


import com.unicast.unicast_backend.assemblers.DisplayResourceAssembler;
import com.unicast.unicast_backend.exceptions.GreaterSecondsException;
import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.DisplayRepository;
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
 * Controlador REST para los displays
 */

@RestController
public class DisplayController {

    // Repositorio de displays
    private final DisplayRepository displayRepository;

    // Repositorio de videos
    private final VideoRepository videoRepository;

    // Repositorio de ensamblado
    private final DisplayResourceAssembler displayAssembler;

    /*
     * Constructor del controlador de displays
     */
    @Autowired
    public DisplayController(DisplayRepository u, VideoRepository videoRepository,
            DisplayResourceAssembler displayAssembler) {
        this.displayRepository = u;
        this.videoRepository = videoRepository;
        this.displayAssembler = displayAssembler;
    }

    /*
     * Permite actualizar un display en un instante de tiempo concreto del video
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param secondsFromBeginning: segundos en los que se desea anyadir el display
     *  @param videoId: codigo identificador del video que se desea anyadir el display
     */ 
    @PostMapping(value = "/api/displays", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> updateDisplay(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("secs_from_beg") Integer secondsFromBeginning, @RequestParam("video_id") Long videoId)
            throws URISyntaxException,GreaterSecondsException{

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Busqueda del video en el repositorio por su id
        Video video = videoRepository.findById(videoId).get();
        if(video.getSeconds() < secondsFromBeginning){
            // Excepcion si los segundos del display superan duracion del video
            throw new GreaterSecondsException("Instante de comentario mayor que la duracion del video");
        }
    
        // Creacion del display
        Display display = new Display();
        display.setSecsFromBeg(secondsFromBeginning);
        display.setUser(user);
        display.setTimestampLastTime(Timestamp.from(Instant.now()));
        display.setVideo(video);

        // Establecer relacion entre el video y el display
        DisplayKey key = new DisplayKey();
        key.setUserId(user.getId());
        key.setVideoId(video.getId());

        display.setId(key);

        // Guardar los cambios en el repositorio
        displayRepository.saveInternal(display);

        // Respuesta de la operacion
        Resource<Display> resourceDisplay = displayAssembler.toResource(display);
        return ResponseEntity.ok(resourceDisplay);
    }

    /*
     * Permite borrar un display de un video
     * Parametros
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param videoId: codigo identificador del video que se desea comentar
     */
    @DeleteMapping(value = "/api/displays/{video_id}")
    public ResponseEntity<?> deleteDisplay(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @PathVariable(name = "video_id", required = true) Long videoId) {
        DisplayKey key = new DisplayKey();
        key.setUserId(userAuth.getId());
        key.setVideoId(videoId);

        displayRepository.deleteById(key);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}