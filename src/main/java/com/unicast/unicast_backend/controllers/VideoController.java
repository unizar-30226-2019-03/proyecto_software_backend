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
import com.unicast.unicast_backend.s3handlers.S3VideoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PostMapping(value = "/videos", produces = "application/json", consumes = "multipart/form-data")
    public @ResponseBody Resource<Video> uploadVideo(@RequestPart("file") MultipartFile video,
            @RequestPart("video") Video videoInfo, @RequestPart("subject_id") Long subjectId)
            throws IllegalStateException, IOException, URISyntaxException {
        URI videoURL = s3VideoHandler.uploadFile(video);

        Subject subject = subjectRepository.findById(subjectId).get();

        videoInfo.setSubject(subject);
        videoInfo.setS3Path(videoURL);
        videoInfo.setTimestamp(Timestamp.from(Instant.now()));

        videoRepository.save(videoInfo);

        return videoAsssembler.toResource(videoInfo);
    }
}
