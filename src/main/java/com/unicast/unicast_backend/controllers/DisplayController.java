package com.unicast.unicast_backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.unicast.unicast_backend.assemblers.DisplayResourceAssembler;
import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.DisplayRepository;
import com.unicast.unicast_backend.persistance.repository.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DisplayController {

    private final DisplayRepository displayRepository;

    private final VideoRepository videoRepository;

    private final DisplayResourceAssembler displayAssembler;

    @Autowired
    public DisplayController(DisplayRepository u, VideoRepository videoRepository,
            DisplayResourceAssembler displayAssembler) {
        this.displayRepository = u;
        this.videoRepository = videoRepository;
        this.displayAssembler = displayAssembler;
    }

    @PostMapping(value = "/api/displays/update", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> updateDisplay(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("secs_from_beg") Integer secondsFromBeginning,
            @RequestParam("video_id") Long videoId)
            throws URISyntaxException {
        User user = userAuth.getUser();

        Display display = new Display();
        display.setSecsFromBeg(secondsFromBeginning);
        display.setUser(user);
        display.setTimestampLastTime(Timestamp.from(Instant.now()));
        Video video = videoRepository.findById(videoId).get();
        display.setVideo(video);

        DisplayKey key = new DisplayKey();
        key.setUserId(user.getId());
        key.setVideoId(video.getId());

        display.setId(key);

        displayRepository.save(display);

        Resource<Display> resourceDisplay = displayAssembler.toResource(display);

        return ResponseEntity.created(new URI(resourceDisplay.getId().expand().getHref())).body(resourceDisplay);
    }
}