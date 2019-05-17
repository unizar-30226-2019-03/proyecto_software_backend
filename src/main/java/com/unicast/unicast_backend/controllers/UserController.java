package com.unicast.unicast_backend.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import com.unicast.unicast_backend.assemblers.UserResourceAssembler;
import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.rest.DegreeRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UniversityRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private RoleRepository roleRepository;

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
            @RequestParam("degree_id") Long degreeId, @RequestPart("photo") MultipartFile photo) {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un
        // usuario con nombre/email iguales

        try {

            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setSurnames(surnames);
            user.setDegree(degreeRepository.findById(degreeId).get());
            user.setPassword(securityConfiguration.passwordEncoder().encode(password));
            user.setEmail(email);
            user.setDescription(description);
            user.setUniversity(universityRepository.findById(universityId).get());
            user.setEnabled(true);
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRolesAndPrivileges(Arrays.asList(userRole));

            URI photoURL = s3ImageHandler.uploadFile(photo);
            user.setPhoto(photoURL);

            s3ImageHandler.deleteLastUploadedTmpFile();
            userRepository.saveInternal(user);

            Resource<User> resourceUser = userAssembler.toResource(user);

            return ResponseEntity.created(new URI(resourceUser.getId().getHref())).body(resourceUser);
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
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "surnames", required = false) String surnames,
            @RequestParam(name = "university_id", required = false) Long universityId,
            @RequestParam(name = "degree_id", required = false) Long degreeId,
            @RequestPart(name = "photo", required = false) MultipartFile photo) throws IOException, URISyntaxException {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un
        // usuario con nombre/email iguales

        // try {
        // User user = userRepository.findByUsername(userAuth.getUsername());
        User user = userAuth.getUser();

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
        if (name != null && !StringUtils.isEmpty(name)) {
            user.setName(name);
        }
        if (surnames != null && !StringUtils.isEmpty(surnames)) {
            user.setSurnames(surnames);
        }
        if (universityId != null) {
            user.setUniversity(universityRepository.findById(universityId).get());
        }
        if (degreeId != null) {
            user.setDegree(degreeRepository.findById(degreeId).get());
        }
        user.setEnabled(true);

        if (photo != null && !photo.isEmpty()) {
            // TODO: borrar foto antigua o lo que sea
            URI photoURL = s3ImageHandler.uploadFile(photo);
            user.setPhoto(photoURL);
            s3ImageHandler.deleteLastUploadedTmpFile();
        }

        userRepository.saveInternal(user);
        return ResponseEntity.ok(userAssembler.toResource(user));
        // } catch (org.springframework.dao.DataIntegrityViolationException e) {
        // ResponseEntity<String> res = new ResponseEntity("El username ya existe",
        // HttpStatus.BAD_REQUEST);
        // return res;
        // }
    }

    @PatchMapping(value = "/api/users/setDisabled")
    public ResponseEntity<?> setDisabled(@AuthenticationPrincipal UserDetailsImpl userAuth) {
        // TODO: gestionar tags y errores
        User user = userAuth.getUser();

        user.setEnabled(false);

        userRepository.saveInternal(user);
        // s3ImageHandler.deleteFile(user.getPhoto());

        // userRepository.delete(user);

        return ResponseEntity.ok().build();
    }
    

    @PatchMapping(value = "/api/users/makeProfessor", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('MAKE_PROFESSOR_PRIVILEGE')")
    public ResponseEntity<?> makeProfessor(@RequestParam("user_id") Long userId) {
        // TODO: gestionar tags y errores
        User user = userRepository.findById(userId).get();
        Role professorRole = roleRepository.findByName("ROLE_PROFESSOR");
        ArrayList<Role> l = new ArrayList<>();
        l.add(professorRole);
        user.setRolesAndPrivileges(l);

        userRepository.saveInternal(user);
        
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/api/users/eraseProfessor", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ERASE_PROFESSOR_PRIVILEGE')")
    public ResponseEntity<?> eraseProfessor(@RequestParam("user_id") Long userId) {
        // TODO: gestionar tags y errores
        User user = userRepository.findById(userId).get();
        Role userRole = roleRepository.findByName("ROLE_USER");
        ArrayList<Role> l = new ArrayList<>();
        l.add(userRole);
        user.setRolesAndPrivileges(l);

        userRepository.saveInternal(user);
        
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/api/users/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long userId)
            throws Exception, IllegalStateException, IOException, URISyntaxException {
        // TODO: gestionar tags y errores
        User user = userRepository.findById(userId).get();

        if (user.getPhoto() != null) {
            s3ImageHandler.deleteFile(user.getPhoto());
        }

        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
