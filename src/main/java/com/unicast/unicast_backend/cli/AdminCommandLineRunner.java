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

@Component
public class AdminCommandLineRunner implements CommandLineRunner {
    @Autowired 
	private SecurityConfiguration securityConfiguration;

	@Autowired 
	private RoleRepository roleRepository;

	@Autowired 
	private UserRepository userRepository;

    @Autowired
    private ReproductionListRepository reproductionListRepository;

    @Override
    public void run(String... args) throws Exception {
        if (args != null && args.length == 3) {
            String username = args[0];
            String password = args[1];
            String email = args[2];
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(email)) {
                if (userRepository.findByUsername(username) == null) {
                    User user = new User();
                    user.setUsername(username);
                    user.setName(username);
                    user.setSurnames(username);
                    user.setEmail(email);
                    user.setPassword(securityConfiguration.passwordEncoder().encode(password));
                    user.setDescription("");
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                    user.setRolesAndPrivileges(Arrays.asList(adminRole));
                    user.setEnabled(true);

                    ReproductionList reproList = new ReproductionList();
                    reproList.setUser(user);
                    reproList.setName("Favoritos");
        
                    reproductionListRepository.save(reproList);
        
                    userRepository.saveInternal(user);
                }
            }
        }
    }
}
