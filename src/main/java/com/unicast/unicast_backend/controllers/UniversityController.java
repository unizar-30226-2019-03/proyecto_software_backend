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

@RestController
public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UniversityResourceAssembler universityAssembler;

    @Autowired
    private S3ImageHandler s3ImageHandler;

    @PostMapping(value = "/api/universities/add", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('CREATE_UNIVERSITY_PRIVILEGE')")
    public ResponseEntity<?> addNewUniversity(@RequestParam("name") String name,
            @RequestPart("photo") MultipartFile photo) {

        try {

            University university = new University();
            university.setName(name);

            URI photoURL = s3ImageHandler.uploadFile(photo);
            university.setPhoto(photoURL);

            s3ImageHandler.deleteLastUploadedTmpFile();

            universityRepository.saveInternal(university);

            Resource<University> resourceUniversity = universityAssembler.toResource(university);

            return ResponseEntity.created(new URI(resourceUniversity.getId().getHref())).body(resourceUniversity);
        } catch (IOException ioE) {
            // TODO: hacer algo
            return ResponseEntity.badRequest().build();
        } catch (URISyntaxException uriE) {
            // TODO: hacer algo
            return ResponseEntity.badRequest().build();
        }
        // } catch (org.springframework.dao.DataIntegrityViolationException e) {
        // ResponseEntity<String> res = new ResponseEntity("El username ya existe",
        // HttpStatus.BAD_REQUEST);
        // return res;
        // }
    }
    
    @DeleteMapping(value = "/api/universities/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    public ResponseEntity<?> deleteUniversity(@RequestParam("id") Long universityId) {
        University university = universityRepository.findById(universityId).get();

        s3ImageHandler.deleteFile(university.getPhoto());

        universityRepository.delete(university);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
