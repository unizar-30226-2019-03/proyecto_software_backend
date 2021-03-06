package com.unicast.unicast_backend.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.NotificationCategory;
import com.unicast.unicast_backend.persistance.model.Privilege;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.repository.PrivilegeRepository;
import com.unicast.unicast_backend.persistance.repository.RoleRepository;
import com.unicast.unicast_backend.persistance.repository.rest.NotificationCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableAsync
@EnableScheduling
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NotificationCategoryRepository notificationCatRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Privilege createVideoPrivilege = createPrivilegeIfNotFound("CREATE_VIDEO_PRIVILEGE");
        Privilege updateVideoPrivilege = createPrivilegeIfNotFound("UPDATE_VIDEO_PRIVILEGE");
        Privilege deleteVideoPrivilege = createPrivilegeIfNotFound("DELETE_VIDEO_PRIVILEGE");

        Privilege createDegreePrivilege = createPrivilegeIfNotFound("CREATE_DEGREE_PRIVILEGE");
        Privilege updateDegreePrivilege = createPrivilegeIfNotFound("UPDATE_DEGREE_PRIVILEGE");
        Privilege deleteDegreePrivilege = createPrivilegeIfNotFound("DELETE_DEGREE_PRIVILEGE");

        Privilege createSubjectPrivilege = createPrivilegeIfNotFound("CREATE_SUBJECT_PRIVILEGE");
        Privilege updateSubjectPrivilege = createPrivilegeIfNotFound("UPDATE_SUBJECT_PRIVILEGE");
        Privilege deleteSubjectPrivilege = createPrivilegeIfNotFound("DELETE_SUBJECT_PRIVILEGE");

        Privilege createUniversityPrivilege = createPrivilegeIfNotFound("CREATE_UNIVERSITY_PRIVILEGE");
        Privilege updateUniversityPrivilege = createPrivilegeIfNotFound("UPDATE_UNIVERSITY_PRIVILEGE");
        Privilege deleteUniversityPrivilege = createPrivilegeIfNotFound("DELETE_UNIVERSITY_PRIVILEGE");

        Privilege addProfessor2SubjectPrivilege = createPrivilegeIfNotFound("ADD_PROFESSOR2SUBJECT_PRIVILEGE");
        Privilege removeProfessor2SubjectPrivilege = createPrivilegeIfNotFound(
                "REMOVE_PROFESSOR_FROM_SUBJECT_PRIVILEGE");

        Privilege eraseProfessorPrivilege = createPrivilegeIfNotFound("ERASE_PROFESSOR_PRIVILEGE");
        Privilege makeProfessorPrivilege = createPrivilegeIfNotFound("MAKE_PROFESSOR_PRIVILEGE");

        Privilege deleteUserPrivilege = createPrivilegeIfNotFound("DELETE_USER_PRIVILEGE");

        List<Privilege> professorPrivileges = Arrays.asList(createVideoPrivilege, updateVideoPrivilege,
                deleteVideoPrivilege);

        List<Privilege> adminPrivileges = Arrays.asList(createDegreePrivilege, updateDegreePrivilege,
                deleteDegreePrivilege, createSubjectPrivilege, updateSubjectPrivilege, deleteSubjectPrivilege,
                createUniversityPrivilege, updateUniversityPrivilege, deleteUniversityPrivilege, deleteUserPrivilege,
                addProfessor2SubjectPrivilege, removeProfessor2SubjectPrivilege, makeProfessorPrivilege,
                eraseProfessorPrivilege);

        List<Privilege> userPrivileges = Arrays.asList();

        createRoleIfNotFound("ROLE_USER", userPrivileges);
        createRoleIfNotFound("ROLE_PROFESSOR", professorPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        // LAS CATEGORIAS DE LAS NOTIFICACIONES SE CREAN AQUI

        if (!notificationCatRepository.findByName("videos").isPresent()) {
            NotificationCategory videosCategory = new NotificationCategory();
            videosCategory.setName("videos");
            notificationCatRepository.saveInternal(videosCategory);
        }

        if (!notificationCatRepository.findByName("messages").isPresent()) {
            NotificationCategory videosCategory = new NotificationCategory();
            videosCategory.setName("messages");
            notificationCatRepository.saveInternal(videosCategory);
        }

        alreadySetup = true;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return  taskScheduler;
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

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }
}
