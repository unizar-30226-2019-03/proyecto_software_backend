package com.unicast.unicast_backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import com.unicast.unicast_backend.assemblers.VoteResourceAssembler;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;
import com.unicast.unicast_backend.persistance.repository.VideoRepository;
import com.unicast.unicast_backend.persistance.repository.VoteRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    private final VoteRepository voteRepository;

    private final VideoRepository videoRepository;

    private final VoteResourceAssembler voteAssembler;

    @Autowired
    public VoteController(VoteRepository u, VideoRepository videoRepository, VoteResourceAssembler voteAssembler) {
        this.voteRepository = u;
        this.videoRepository = videoRepository;
        this.voteAssembler = voteAssembler;
    }

    @PostMapping(value = "/api/votes", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> updateDisplay(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("clarity") Integer clarity, @RequestParam("quality") Integer quality,
            @RequestParam("suitability") Integer suitability, @RequestParam("video_id") Long videoId)
            throws URISyntaxException {
        User user = userAuth.getUser();

        Video video = videoRepository.findById(videoId).get();

        VoteKey voteId = new VoteKey();
        voteId.setUserId(user.getId());
        voteId.setVideoId(video.getId());

        Vote vote = new Vote();
        vote.setId(voteId);
        vote.setClarity(clarity);
        vote.setQuality(quality);
        vote.setSuitability(suitability);
        vote.setUser(user);
        vote.setVideo(video);

        voteRepository.save(vote);

        Resource<Vote> resourceVote = voteAssembler.toResource(vote);

        return ResponseEntity.created(new URI(resourceVote.getId().expand().getHref())).body(resourceVote);
    }
}