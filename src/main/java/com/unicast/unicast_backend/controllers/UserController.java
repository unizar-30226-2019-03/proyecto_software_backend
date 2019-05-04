package com.unicast.unicast_backend.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.unicast.unicast_backend.assemblers.UserResourceAssembler;
import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.DegreeRepository;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.UniversityRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
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
    private DegreeRepository degreeRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserResourceAssembler userAssembler;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private S3ImageHandler s3ImageHandler;

    @PostMapping(value = "/api/public/register", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> registerNewUser(@RequestParam("username") String username,
            @RequestParam("name") String name, @RequestParam("surnames") String surnames,
            @RequestParam("password") String password, @RequestParam("description") String description,
            @RequestParam("email") String email, @RequestParam("university_id") Long universityId,
            @RequestParam("degree_id") Long degreeId,
            @RequestPart("photo") MultipartFile photo) {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un
        // usuario con nombre/email iguales

        try {
            URI photoURL = s3ImageHandler.uploadFile(photo);

            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setSurnames(surnames);
            user.setDegree(degreeRepository.findById(degreeId).get());
            user.setPassword(securityConfiguration.passwordEncoder().encode(password));
            user.setEmail(email);
            user.setDescription(description);
            user.setPhoto(photoURL);
            user.setUniversity(universityRepository.findById(universityId).get());
            user.setEnabled(true);

            userRepository.save(user);

            Resource<User> resourceUser = userAssembler.toResource(user);

            return ResponseEntity.created(new URI(resourceUser.getId().getHref()))
                    .body(resourceUser);
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

    @PostMapping(value = "/api/users/update", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "university_id", required = false) Long universityId,
            @RequestParam(name = "degree_id", required = false) Long degreeId,
            @RequestPart(name = "photo", required = false) MultipartFile photo) throws IOException, URISyntaxException {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un
        // usuario con nombre/email iguales

        // try {
        // User user = userRepository.findByUsername(userAuth.getUsername());
        User user = userAuth.getUser();

        if (photo != null && !photo.isEmpty()) {
            // TODO: borrar foto antigua o lo que sea
            URI photoURL = s3ImageHandler.uploadFile(photo);
            user.setPhoto(photoURL);
        }
        if (username != null && !StringUtils.isEmpty(username)) {
            user.setUsername(username);
        }
        if (password != null && !StringUtils.isEmpty(password)) {
            user.setPassword(securityConfiguration.passwordEncoder().encode(password));
        }
        if (email != null && !StringUtils.isEmpty(email)) {
            user.setEmail(email);
        }
        if (description != null && !StringUtils.isEmpty(description)) {
            user.setDescription(description);
        }
        if (universityId != null) {
            user.setUniversity(universityRepository.findById(universityId).get());
        }
        if (degreeId != null) {
            user.setDegree(degreeRepository.findById(degreeId).get());
        }
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
