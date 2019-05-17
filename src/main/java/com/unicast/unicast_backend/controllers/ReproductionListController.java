package com.unicast.unicast_backend.controllers;

import java.net.URI;

import com.unicast.unicast_backend.assemblers.ReproductionListAssembler;
import com.unicast.unicast_backend.persistance.model.Contains;
import com.unicast.unicast_backend.persistance.model.ContainsKey;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.ContainsRepository;
import com.unicast.unicast_backend.persistance.repository.rest.ReproductionListRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReproductionListController {

    private final ReproductionListRepository reproductionListRepository;

    private final VideoRepository videoRepository;

    private final ReproductionListAssembler reproductionListAssembler;

    private final ContainsRepository containsRepository;

    @Autowired
    public ReproductionListController(ReproductionListRepository u, ReproductionListAssembler assembler,
            VideoRepository videoRepository, ContainsRepository containsRepository) {
        this.reproductionListRepository = u;
        this.reproductionListAssembler = assembler;
        this.videoRepository = videoRepository;
        this.containsRepository = containsRepository;
    }

    @PostMapping(value = "/api/reproductionLists/add", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("name") String name) throws Exception {

        // Obtencion de los datos del usuario loggeado
        User user = userAuth.getUser();

        ReproductionList reproList = new ReproductionList();
        reproList.setUser(user);
        reproList.setName(name);

        reproductionListRepository.save(reproList);

        Resource<ReproductionList> resourceReproList = reproductionListAssembler.toResource(reproList);

        return ResponseEntity.created(new URI(resourceReproList.getId().expand().getHref())).body(resourceReproList);
    }

    @DeleteMapping(value = "/api/reproductionLists/{id}")
    public ResponseEntity<?> deleteReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @PathVariable(name = "id", required = true) Long reproListId) {

        User user = userAuth.getUser();

        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        if (reproList.getUser().getId() != user.getId()) {
            // TODO: gestionar excepcion en condiciones
            throw new Error();
        }

        reproductionListRepository.delete(reproList);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/api/reproductionLists/addVideo", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> anyadirVideoReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("repro_list_id") Long reproListId, @RequestParam("video_id") Long videoId) throws Exception {
        User user = userAuth.getUser();

        Video video = videoRepository.findById(videoId).get();
        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        if (reproList.getUser().getId() != user.getId()) {
            // TODO: gestionar excepcion en condiciones
            throw new Error();
        }

        // Crear el contains Key
        ContainsKey ck = new ContainsKey();
        ck.setListId(reproList.getId());
        ck.setVideoId(video.getId());

        // Crear contains
        Contains c = new Contains();

        c.setId(ck);
        c.setList(reproList);
        c.setVideo(video);
        c.setPosition(reproList.getVideoList().size() + 1);

        containsRepository.save(c);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/api/reproductionLists/deleteVideo", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> deleteReproductionList(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("repro_list_id") Long reproListId, @RequestParam("video_id") Long videoId) throws Exception {

        // Obtencion de los datos del usuario loggeado
        User user = userAuth.getUser();

        ReproductionList reproList = reproductionListRepository.findById(reproListId).get();

        if (reproList.getUser().getId() != user.getId()) {
            // TODO: gestionar excepcion en condiciones
            throw new Error();
        }

        ContainsKey ck = new ContainsKey();
        ck.setListId(reproListId);
        ck.setVideoId(videoId);

        containsRepository.deleteById(ck);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}