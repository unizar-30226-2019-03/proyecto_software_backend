/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.services;

import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Permite la gestion de los datos de servicio de los usuarios
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio de usuarios
    @Autowired
    private UserRepository userRepository;


    /*
     * Permite cargar y buscar los detalles de servicio de un usuario concreto en funcion de su identificador
     * @param userId: identificador del usuario del que se desean cargar sus detalles de servicio
     */
    public UserDetails loadUserById(Long userId) {

        // Localizar el usuario en el repositoeio por si id
        Optional<User> user = userRepository.findById(userId);

        // Lanzar excepcion en caso de no presencia del usuario en el repositorio
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(userId.toString());
        }

        // Devolver los detalles de servicio del usuario 
        return new UserDetailsImpl(user.get());
    }


    /*
     * Permite cargar y buscar los detalles de servicio de un usuario concreto en funcion de su nombre de usuario
     * @param username: nombre de usuario del usuario del que se desean cargar sus detalles de servicio
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        // Localizar el usuario en el repositoeio por si nombre de usuario
        User user = userRepository.findByUsername(username);

        // Lanzar excepcion en caso de no presencia del usuario en el repositorio
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        // Devolver los detalles de servicio del usuario
        return new UserDetailsImpl(user);
    }
}