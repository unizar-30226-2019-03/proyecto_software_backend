package com.unicast.unicast_backend.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.unicast.unicast_backend.assemblers.VideoResourceAssembler;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;
import com.unicast.unicast_backend.s3handlers.S3VideoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private VideoResourceAssembler videoAsssembler;

    @Autowired
    private S3VideoHandler s3VideoHandler;

    @Autowired
    private S3ImageHandler s3ImageHandler;

    @PostMapping(value = "/api/upload/video", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadVideo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestPart("file") MultipartFile videoFile, @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestParam("title") String title, @RequestParam("description") String description,
            @RequestParam("subject_id") Long subjectId) throws IllegalStateException, IOException, URISyntaxException {
        // TODO: gestionar tags
        Video video = new Video();

        Subject subject = subjectRepository.findById(subjectId).get();

        video.setTitle(title);
        video.setDescription(description);
        video.setSubject(subject);
        video.setUploader(userAuth.getUser());
        video.setTimestamp(Timestamp.from(Instant.now()));
        
        URI videoURL = s3VideoHandler.uploadFile(videoFile);
        URI thumbnailURL = s3ImageHandler.uploadFile(thumbnail);
        
        video.setUrl(videoURL);
        video.setThumbnailUrl(thumbnailURL);

        videoRepository.save(video);

        Resource<Video> resourceVideo = videoAsssembler.toResource(video);

        return ResponseEntity.created(new URI(resourceVideo.getId().expand().getHref())).body(resourceVideo);
    }
}
