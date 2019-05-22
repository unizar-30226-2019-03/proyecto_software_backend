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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.unicast.unicast_backend.assemblers.UniversityResourceAssembler;
import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.repository.rest.UniversityRepository;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
 * Controlador REST de universidades
 */
@RestController
public class UniversityController {

    // Repositorio de universidades
    @Autowired
    private UniversityRepository universityRepository;

    // Ensamblador
    @Autowired
    private UniversityResourceAssembler universityAssembler;

    // Rutinas de carga de imagenes
    @Autowired
    private S3ImageHandler s3ImageHandler;


    /*
     * Permite anyadir una nueva universidad
     * Parametros:
     *  @param nombre: nombre de la universidad
     *  @param photo: miniatura de la universidad
     */
    @PostMapping(value = "/api/universities/add", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('CREATE_UNIVERSITY_PRIVILEGE')")
    public ResponseEntity<?> addNewUniversity(@RequestParam("name") String name,
            @RequestPart("photo") MultipartFile photo) throws IOException,URISyntaxException{

            // Creacion de la universidad y asignacion del nombre
            University university = new University();
            university.setName(name);

            // Cargar la miniatura de la universidad
            URI photoURL = s3ImageHandler.uploadFile(photo);
            university.setPhoto(photoURL);
            s3ImageHandler.deleteLastUploadedTmpFile();

            // Guardar los cambios en el repositorio
            universityRepository.saveInternal(university);

            // Envio de respuesta
            Resource<University> resourceUniversity = universityAssembler.toResource(university);
            return ResponseEntity.created(new URI(resourceUniversity.getId().getHref())).body(resourceUniversity);
    }
    

    /*
     * Permite borrar una universidad
     * Parametros:
     *  @param universityId: identificador de la universidad a borrar
     */
    @DeleteMapping(value = "/api/universities/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    public ResponseEntity<?> deleteUniversity(@RequestParam("id") Long universityId) {

        // Busqueda de la universidad por su id en el repositorio
        University university = universityRepository.findById(universityId).get();

        // Borrar miniatura
        s3ImageHandler.deleteFile(university.getPhoto());

        // Borrar universidad del repositorio y enviar respuesta
        universityRepository.delete(university);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
