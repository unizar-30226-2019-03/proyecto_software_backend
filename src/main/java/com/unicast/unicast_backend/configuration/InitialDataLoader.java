package com.unicast.unicast_backend.configuration;

import java.util.Collection;
import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.Privilege;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.PrivilegeRepository;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    private boolean alreadySetup = false;
 
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;
  
    @Autowired
    private PrivilegeRepository privilegeRepository;
  
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup) {
            return;
        }


        // TODO: cambiar y crear la cuenta de admin en condiciones
        Optional<User> userOpt = userRepository.findById(1L);

        if (!userOpt.isPresent()) {
            User user = new User();
            user.setId(1L);
            user.setUsername("admin");
            user.setPassword(securityConfiguration.passwordEncoder().encode("password1234"));
            user.setEmail("a@a.com");
            user.setDescription("blablabla");
            user.setEnabled(true);
            userRepository.save(user);
        }


        // TODO: crear privilegios y roles apropiados
        // Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        // Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
  
        // List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);        
        // createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        // createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));
 
        alreadySetup = true;
    }
 
    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
  
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
 
    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
  
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
