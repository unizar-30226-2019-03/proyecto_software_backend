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
import java.sql.Timestamp;
import java.time.Instant;

import com.unicast.unicast_backend.assemblers.CommentResourceAssembler;
import com.unicast.unicast_backend.exceptions.GreaterSecondsException;
import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.CommentRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST para la gestion de comentarios
 */

@RestController
public class CommentController {

    // Repositorio JPA para los comentarios
    private final CommentRepository commentRepository;

    // Repositorio JPA para los videos
    private final VideoRepository videoRepository;

    // Repositorio JPA de ensamblado
    private final CommentResourceAssembler commentAssembler;

    /*
     * Constructor del controlador REST
     */
    @Autowired
    public CommentController(CommentRepository u, VideoRepository videoRepository,
            CommentResourceAssembler commentAssembler) {
        this.commentRepository = u;
        this.videoRepository = videoRepository;
        this.commentAssembler = commentAssembler;
    }

    
    /*
     * Permite anyadir un comentario en un instante de tiempo concreto del video
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param secondsFromBeginning: segundos en los que se desea comentar
     *  @param videoId: codigo identificador del video que se desea comentar
     */
    @PostMapping(value = "/api/comments", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addNewComment(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("text") String text, @RequestParam("secs_from_beg") Integer secondsFromBeginning,
            @RequestParam("video_id") Long videoId,
            @RequestParam(name = "comment_replied_to_id", required = false) Long commentRepliedToId)
            throws URISyntaxException,GreaterSecondsException {
        
        // Extraccion de los datos de usuario
        User user = userAuth.getUser();

        // Crear comentario a anyadir
        Comment comment = new Comment();

        // Encontrar el video por medio del id que se desea buscar en el repositorio
        Video video = videoRepository.findById(videoId).get();

        if(video.getSeconds()< secondsFromBeginning){
            // Excepcion si el comentario se va a hacer fuera de la duracion del video
            throw new GreaterSecondsException("Instante de comentario mayor que la duracion del video");
        }

        // Anyadir atributos al comentario
        comment.setText(text);
        comment.setSecondsFromBeginning(secondsFromBeginning);
        comment.setTimestamp(Timestamp.from(Instant.now()));   
        comment.setVideo(video);

        // Gestion de respuestas a ese comentario
        if (commentRepliedToId != null) {
            Comment commentRepliedTo = commentRepository.findById(commentRepliedToId).get();
            comment.setCommentRepliedTo(commentRepliedTo);
        }

        // Asignar el usuario al comentario
        comment.setUser(user);

        // Guardar cambios en el repositorio
        commentRepository.saveInternal(comment);

        // Devolver respuesta de la operacion
        Resource<Comment> resourceComment = commentAssembler.toResource(comment);
        return ResponseEntity.created(new URI(resourceComment.getId().expand().getHref())).body(resourceComment);
    }
}