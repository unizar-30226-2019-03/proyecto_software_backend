/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.cli;

import java.util.Arrays;

import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.rest.ReproductionListRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
 * Creacion de usuarios con permisos de administracion
 */

@Component
public class AdminCommandLineRunner implements CommandLineRunner {

    // Seguridad
    @Autowired 
	private SecurityConfiguration securityConfiguration;

    // Repositorio de roles
	@Autowired 
	private RoleRepository roleRepository;

    // Repositorio de usuarios
	@Autowired 
    private UserRepository userRepository;

    // Repositorio de listas de reproduccion de videos
    @Autowired
    private ReproductionListRepository reproductionListRepository;

    /*
     * Permite proporcionar a un usuario permisos de administrador
     * Paranetros
     * @param username: nombre de usuario a anyadir como administrador
     * @param password: contrasenya del usuario a anyadir como administrador
     * @param email: correo del usuario a anyadir como administrador
     */
    @Override
    public void run(String... args) throws Exception {
        if (args != null && args.length >= 3) {
            // Comprobar que se reciben los tres argumentos
            String username = args[0];
            String password = args[1];
            String email = args[2];
            
            // Comprobar que los tres campos no son nulos
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(email)) {

                // Si no existe ese usuario en el repositorio se crea uno nuevo con 
                // los permisos de administrador
                if (userRepository.findByUsername(username) == null) {

                    // Asignacion de atributos
                    User user = new User();
                    user.setUsername(username);
                    user.setName(username);
                    user.setSurnames(username);
                    user.setEmail(email);
                    user.setPassword(securityConfiguration.passwordEncoder().encode(password));
                    user.setDescription("");

                    // Dar permisos al usuario de administrador
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                    user.setRolesAndPrivileges(Arrays.asList(adminRole));

                    // Activacion del usuario
                    user.setEnabled(true);

                    // Guardar el nuevo usuario en el repositorio
                    userRepository.saveInternal(user);
                    
                    // Crear por defecto una lista de reproduccion de videos
                    ReproductionList reproList = new ReproductionList();
                    reproList.setUser(user);
                    reproList.setName("Favoritos");
                    
                    reproductionListRepository.save(reproList);
                }
            }
        }
    }
}
