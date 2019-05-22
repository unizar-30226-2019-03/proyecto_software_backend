package com.unicast.unicast_backend.controllers;

import com.unicast.unicast_backend.exceptions.NotProfessorException;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @PutMapping(value = "/api/subjects/follow", consumes = "multipart/form-data")
    public ResponseEntity<?> followSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId).get();
        User user = userAuth.getUser();
        subject.getFollowers().add(user);
        subjectRepository.saveInternal(subject);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/api/subjects/unfollow", consumes = "multipart/form-data")
    public ResponseEntity<?> unfollowSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId).get();
        User user = userAuth.getUser();
        subject.getFollowers().remove(user);
        subjectRepository.saveInternal(subject);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/api/subjects/professors", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ADD_PROFESSOR2SUBJECT_PRIVILEGE')")
    public ResponseEntity<?> addProfessorToSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId, @RequestParam("professor_id") Long professorId) throws NotProfessorException{

        Subject subject = subjectRepository.findById(subjectId).get();
        User user = userRepository.findById(professorId).get();
        if (user.getRole() != "ROLE_PROFESSOR") {
            throw new NotProfessorException("El usuario no es profesor");
        }
        subject.getProfessors().add(user);
        subjectRepository.saveInternal(subject);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/api/subjects/professors", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('REMOVE_PROFESSOR_FROM_SUBJECT_PRIVILEGE')")
    public ResponseEntity<?> removeProfessorFromSubject(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("subject_id") Long subjectId, @RequestParam("professor_id") Long professorId) {

        Subject subject = subjectRepository.findById(subjectId).get();
        User user = userRepository.findById(professorId).get();
        subject.getProfessors().remove(user);
        subjectRepository.saveInternal(subject);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
