package com.unicast.unicast_backend.controllers;

import java.util.Collection;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class SubjectController {

    private final SubjectRepository repo;

    @Autowired
    public SubjectController(SubjectRepository u) {
        this.repo = u;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/subjects/ranking")
    public @ResponseBody ResponseEntity<?> getRanking() {
        List<Subject> producers = repo.findAll();
        Collection<Video> videos;
        Collection<Vote> votes;
        for(Subject s: producers){
            videos=s.getVideos();
            for(Video v:videos){
                votes=v.getVotes();
                for(Vote vo:votes){

                }
            }
        }
        //Obtener los videos y comprobar las puntiaciones para hacer los rankings

        return ResponseEntity.ok(producers); 
    
    }

    @RequestMapping(method = RequestMethod.GET, value = "/subjects/points")
    @ResponseBody
    public Subject getPoints(@RequestParam String name){
        Subject s;
        s  = repo.findByName(name);
        s.setPoints(7.2);
        return s;

    }



}