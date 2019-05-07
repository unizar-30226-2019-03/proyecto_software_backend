package com.unicast.unicast_backend.controllers;

import java.util.Collection;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectRepository u, UserRepository userRepository) {
        this.subjectRepository = u;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/subjects/ranking")
    public @ResponseBody ResponseEntity<?> getRanking() {
        List<Subject> producers = subjectRepository.findAll();
        Collection<Video> videos;
        Collection<Vote> votes;
        for (Subject s : producers) {
            videos = s.getVideos();
            for (Video v : videos) {
                votes = v.getVotes();
                for (Vote vo : votes) {

                }
            }
        }
        // Obtener los videos y comprobar las puntiaciones para hacer los rankings

        return ResponseEntity.ok(producers);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/subjects/points")
    @ResponseBody
    public Subject getPoints(@RequestParam String name) {
        Subject s;
        s = subjectRepository.findByNameIgnoreCase(name);
        s.setPoints(7.2);
        return s;

    }

}