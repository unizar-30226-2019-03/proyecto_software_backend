package com.unicast.unicast_backend.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.ConstraintViolationException;

import com.unicast.unicast_backend.assemblers.UserResourceAssembler;
import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.UniversityRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserResourceAssembler userAssembler;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private S3ImageHandler s3ImageHandler;

    @PostMapping(value = "/api/public/register", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> registerNewUser(@RequestParam("username") String username,
            @RequestParam("password") String password, @RequestParam("description") String description,
            @RequestParam("email") String email, @RequestParam("university_id") Long universityId, @RequestPart("photo") MultipartFile photo)
            throws IOException, URISyntaxException {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un
        // usuario con nombre/email iguales

        // try {
        URI photoURL = s3ImageHandler.uploadFile(photo);

        User user = new User();
        user.setUsername(username);
        user.setPassword(securityConfiguration.passwordEncoder().encode(password));
        user.setEmail(email);
        user.setDescription(description);
        user.setPhoto(photoURL);
        user.setUniversity(universityRepository.findById(universityId).get());
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok(userAssembler.toResource(user));
        // } catch (org.springframework.dao.DataIntegrityViolationException e) {
        // ResponseEntity<String> res = new ResponseEntity("El username ya existe",
        // HttpStatus.BAD_REQUEST);
        // return res;
        // }
    }
}
