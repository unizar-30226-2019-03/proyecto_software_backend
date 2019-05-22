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
import java.net.URISyntaxException;

import com.unicast.unicast_backend.assemblers.VoteResourceAssembler;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VoteRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST de los votos
 */

@RestController
public class VoteController {

    // Repositorio de los votos
    private final VoteRepository voteRepository;

    // Repositorio de los videos
    private final VideoRepository videoRepository;

    // Ensamblado
    private final VoteResourceAssembler voteAssembler;

    /*
     * Constructor del controlador REST de los videos
     */
    @Autowired
    public VoteController(VoteRepository u, VideoRepository videoRepository, VoteResourceAssembler voteAssembler) {
        this.voteRepository = u;
        this.videoRepository = videoRepository;
        this.voteAssembler = voteAssembler;
    }


    /*
     * Permite actualizar un display de un video
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param clarity: claridad del video
     * @param quality: calidad del video
     * @param suitability: amenidad del video
     * @param videoId: identificador del video
     */
    @PostMapping(value = "/api/votes", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> updateDisplay(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("clarity") Integer clarity, @RequestParam("quality") Integer quality,
            @RequestParam("suitability") Integer suitability, @RequestParam("video_id") Long videoId)
            throws URISyntaxException {

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Encontrar video en el repositorio
        Video video = videoRepository.findById(videoId).get();

        // Crear el voto del video
        VoteKey voteId = new VoteKey();
        voteId.setUserId(user.getId());
        voteId.setVideoId(video.getId());

        // Asignacion de los atributos
        Vote vote = new Vote();
        vote.setId(voteId);
        vote.setClarity(clarity);
        vote.setQuality(quality);
        vote.setSuitability(suitability);
        vote.setUser(user);
        vote.setVideo(video);

        //Guardar cambios en el repositorio y envio de respuesta de operacion
        voteRepository.saveInternal(vote);
        Resource<Vote> resourceVote = voteAssembler.toResource(vote);
        return ResponseEntity.created(new URI(resourceVote.getId().expand().getHref())).body(resourceVote);
    }
}