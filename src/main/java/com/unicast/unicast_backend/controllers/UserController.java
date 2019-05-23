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
import java.util.ArrayList;
import java.util.Arrays;

import com.unicast.unicast_backend.assemblers.UserResourceAssembler;
import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.rest.DegreeRepository;
import com.unicast.unicast_backend.persistance.repository.rest.ReproductionListRepository;
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

/*
 * Controlador REST para los usuarios
 */

@RestController
public class UserController {

    // Repositorio para los usuarios
    @Autowired
    private UserRepository userRepository;

    // Repositorio para las universidades
    @Autowired
    private UniversityRepository universityRepository;

    // Repositorio para las listas de reproduccion
    @Autowired
    private ReproductionListRepository reproductionListRepository;

    // Repositorio para las carreras
    @Autowired
    private DegreeRepository degreeRepository;

    // Repositorio de los roles de los usuarios
    @Autowired
    private RoleRepository roleRepository;

    // Ensamblador
    @Autowired
    private UserResourceAssembler userAssembler;

    // Configuracion de seguridad
    @Autowired
    private SecurityConfiguration securityConfiguration;

    // Controlador de rutinas de imagenes
    @Autowired
    private S3ImageHandler s3ImageHandler;


    /*
     * Permite registrar un usuario 
     * Parametros
     * @param username: nombre de usuario del usuario
     * @param name: nombre real del usuario
     * @param surnames: apellidos del usuario
     * @param password: contrasenya del usuario
     * @param description: descripcion del usuario
     * @param email: correo del usuario
     * @param universitiyId: identificador de la universidad a la que pertenece
     * @param degreeId: identificador de la carrera que esta estudiando
     * @param photo: foto de perfil del usuario
     */
    @PostMapping(value = "/api/public/register", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> registerNewUser(@RequestParam("username") String username,
            @RequestParam("name") String name, @RequestParam("surnames") String surnames,
            @RequestParam("password") String password, @RequestParam("description") String description,
            @RequestParam("email") String email, @RequestParam("university_id") Long universityId,
            @RequestParam("degree_id") Long degreeId, @RequestPart("photo") MultipartFile photo) throws IOException,URISyntaxException{

            // Creacion de un usuario y asignacion de atributos
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

            // Cargar la foto de perfil
            URI photoURL = s3ImageHandler.uploadFile(photo);
            user.setPhoto(photoURL);
            s3ImageHandler.deleteLastUploadedTmpFile();
            userRepository.saveInternal(user);

            // Iniciar una lista de reproduccion de videos
            ReproductionList reproList = new ReproductionList();
            reproList.setUser(user);
            reproList.setName("Favoritos");

            // Guardar cambios en el repositorio y enviar respuesta
            reproductionListRepository.save(reproList);
            Resource<User> resourceUser = userAssembler.toResource(user);
            return ResponseEntity.created(new URI(resourceUser.getId().getHref())).body(resourceUser);
    }


     /*
     * Permite actualizar los datos de un usuario ya registrado
     * Parametros
     * param userAuth: token con los datos del usuario loggeado
     * @param username: nombre de usuario del usuario
     * @param name: nombre real del usuario
     * @param surnames: apellidos del usuario
     * @param password: contrasenya del usuario
     * @param description: descripcion del usuario
     * @param email: correo del usuario
     * @param universitiyId: identificador de la universidad a la que pertenece
     * @param degreeId: identificador de la carrera que esta estudiando
     * @param photo: foto de perfil del usuario
     */
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

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Actualizacion de los atributos que se desean modificar

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
            s3ImageHandler.deleteFile(user.getPhoto());
            URI photoURL = s3ImageHandler.uploadFile(photo);
            user.setPhoto(photoURL);
            s3ImageHandler.deleteLastUploadedTmpFile();
        }

        // Guardar cambios en el repositorio y enviar respuesta de la operacion
        userRepository.saveInternal(user);
        return ResponseEntity.ok(userAssembler.toResource(user));
    }


    /*
     * Permite deshabilitar un usuario
     * @param userAuth: token con los datos del usuario loggeado
     */
    @PatchMapping(value = "/api/users/setDisabled")
    public ResponseEntity<?> setDisabled(@AuthenticationPrincipal UserDetailsImpl userAuth) {

        // Extraccion de los datos del usuario loggeado
        User user = userAuth.getUser();

        // Deshabilitar al usuario
        user.setEnabled(false);

        // Guardar cambios en el repositorio y enviar la respuesta de la operacion
        userRepository.saveInternal(user);
        return ResponseEntity.ok().build();
    }
    

    /*
     * Permite convertir a un usuario en profesor
     * @param userId: identificador del usuario que se desea convertir en profesor
     */
    @PatchMapping(value = "/api/users/makeProfessor", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('MAKE_PROFESSOR_PRIVILEGE')")
    public ResponseEntity<?> makeProfessor(@RequestParam("user_id") Long userId) {

        // Buscar usuario por id en el repositorio
        User user = userRepository.findById(userId).get();

        // Encontrar el usuario y darle permisos de profesor
        Role professorRole = roleRepository.findByName("ROLE_PROFESSOR");
        ArrayList<Role> l = new ArrayList<>();
        l.add(professorRole);
        user.setRolesAndPrivileges(l);

        // Guardar cambios en el repositorio y devolver respuesta de la operacion
        userRepository.saveInternal(user);
        return ResponseEntity.ok().build();
    }


    /*
     * Permite eliminar a un usuario de profesor
     * @param userId: identificador del usuario que se desea eliminar de  profesor
     */
    @PatchMapping(value = "/api/users/eraseProfessor", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ERASE_PROFESSOR_PRIVILEGE')")
    public ResponseEntity<?> eraseProfessor(@RequestParam("user_id") Long userId) {

        // Buscar usuario por id en el repositorio
        User user = userRepository.findById(userId).get();

        // Encontrar el usuario y darle permisos de profesor
        Role userRole = roleRepository.findByName("ROLE_USER");
        ArrayList<Role> l = new ArrayList<>();
        l.add(userRole);
        user.setRolesAndPrivileges(l);

        // Guardar cambios en el repositorio y devolver respuesta de la operacion
        userRepository.saveInternal(user);
        return ResponseEntity.ok().build();
    }


    /*
     * Permite borrar un usuario
     * @param userId: identificador del usuario a borrar
     */
    @DeleteMapping(value = "/api/users/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long userId)
            throws Exception, IllegalStateException, IOException, URISyntaxException {

        // Buscar usuario por id en el repositorio
        User user = userRepository.findById(userId).get();

        // Eliminar miniatura del usuario
        if (user.getPhoto() != null) {
            s3ImageHandler.deleteFile(user.getPhoto());
        }

        // Borrar usuario del repositorio
        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
