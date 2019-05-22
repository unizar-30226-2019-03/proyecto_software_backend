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

import com.unicast.unicast_backend.exceptions.NotProfessorException;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST para las asignaturas
 */
@RestController
public class SubjectController {

    // Repositorio para los usuarios
    @Autowired
    private UserRepository userRepository;

    // Repositorio para las asignaturas
    @Autowired
    private SubjectRepository subjectRepository;

    /*
     * Permite a un usuario seguir una asignatura
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param subjectId: identificador de la asignatura a seguir
     */
    @PutMapping(value = "/api/subjects/follow", consumes = "multipart/form-data")
    public ResponseEntity<?> followSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId) {

        // Busqueda de la asignatura en el repositorio por id
        Subject subject = subjectRepository.findById(subjectId).get();

        // Extraccion de los datod del usuario
        User user = userAuth.getUser();

        // Anyadir al usuario a la lista de seguidores y guardar cambios en el repositorio
        subject.getFollowers().add(user);
        subjectRepository.saveInternal(subject);

        // Envio de respuesta de la operacion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Permite a un usuario dejar de seguir una asignatura
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param subjectId: identificador de la asignatura a dejar de seguir
     */
    @DeleteMapping(value = "/api/subjects/unfollow", consumes = "multipart/form-data")
    public ResponseEntity<?> unfollowSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId) {

        // Busqueda de la asignatura en el repositorio por id
        Subject subject = subjectRepository.findById(subjectId).get();

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Eliminar usuario de la lista de seguidores y actualizar cambios en el repositorio
        subject.getFollowers().remove(user);
        subjectRepository.saveInternal(subject);

        // Envio de respuesta de la operacion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /*
     * Permite anyadir un profesor a una asignatura
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param subjectId: identificador de la asignatura
     * @param professorId: identificador del profesor a anayadir a la asignatura
     */
    @PutMapping(value = "/api/subjects/professors", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ADD_PROFESSOR2SUBJECT_PRIVILEGE')")
    public ResponseEntity<?> addProfessorToSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId, @RequestParam("professor_id") Long professorId) throws NotProfessorException{

        // Encontrar asignatura en el repositorio por su id
        Subject subject = subjectRepository.findById(subjectId).get();

        // Buscar usuario a anyadir a la asignatura y controlar que sea un profesor
        User user = userRepository.findById(professorId).get();
        if (user.getRole() != "ROLE_PROFESSOR") {
            throw new NotProfessorException("El usuario no es profesor");
        }

        // Anyadir profesor a la lista de profesores asociados y guardar cambios en el repositorio
        subject.getProfessors().add(user);
        subjectRepository.saveInternal(subject);

        // Envio de respuesta de la operaicon
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /*
     * Permite borrar un profesor de una asignatura
     * Parametros
     * @param userAuth: token con los datos del usuario loggeado
     * @param subjectId: identificador de la asignatura
     * @param professorId: identificador del profesor a eliminar de la asignatura
     */
    @DeleteMapping(value = "/api/subjects/professors", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('REMOVE_PROFESSOR_FROM_SUBJECT_PRIVILEGE')")
    public ResponseEntity<?> removeProfessorFromSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId, @RequestParam("professor_id") Long professorId) {

        // Encontrar la asignatura en el repositorio por su id
        Subject subject = subjectRepository.findById(subjectId).get();

        // Buscar el usuario profesor a quitar por su id en el repositorio
        User user = userRepository.findById(professorId).get();

        // Eliminar profesor de la lista de seguidores de la asignatura y guardar cambios en el repositorio
        subject.getProfessors().remove(user);
        subjectRepository.saveInternal(subject);
        
        // Envio de respuesta de operacion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
