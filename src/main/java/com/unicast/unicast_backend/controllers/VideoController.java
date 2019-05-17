package com.unicast.unicast_backend.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;
import com.unicast.unicast_backend.assemblers.VideoResourceAssembler;
import com.unicast.unicast_backend.async.NotificationAsync;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;
import com.unicast.unicast_backend.s3handlers.S3ImageHandler;
import com.unicast.unicast_backend.s3handlers.S3VideoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private NotificationAsync notificationAsync;
    
    @PostMapping(value = "/api/videos/upload", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('CREATE_VIDEO_PRIVILEGE')")
    public ResponseEntity<?> uploadVideo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestPart("file") MultipartFile videoFile, @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestParam("title") String title, @RequestParam("description") String description,
            @RequestParam("subject_id") Long subjectId)
            throws Exception, IllegalStateException, IOException, URISyntaxException {
        // TODO: gestionar tags y errores
        User user = userAuth.getUser();
        Video video = new Video();

        Subject subject = subjectRepository.findById(subjectId).get();

        if (!subject.getProfessors().contains(user) /* y no es profesor el que sube */) {
            // TODO: hacer eso una nueva clase/loqsea
            throw new Exception("El que sube video debe pertenecer a la asignatura del video");
        }

        Timestamp now = Timestamp.from(Instant.now());

        video.setTitle(title);
        video.setDescription(description);
        video.setSubject(subject);
        video.setUploader(user);
        video.setTimestamp(now);

        URI videoURL = s3VideoHandler.uploadFile(videoFile);
        URI thumbnailURL = s3ImageHandler.uploadFile(thumbnail);

        video.setUrl(videoURL);
        video.setThumbnailUrl(thumbnailURL);

        File tempFile = s3VideoHandler.getLastUploadedTmpFile();
        Metadata metadata = ImageMetadataReader.readMetadata(tempFile);

        Mp4Directory mp4Directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);

        if (mp4Directory == null) {
            // TODO: excepcion, el fichero no es video
        }
        
        video.setSeconds(mp4Directory.getInteger(Mp4Directory.TAG_DURATION));

        s3VideoHandler.deleteLastUploadedTmpFile();
        s3ImageHandler.deleteLastUploadedTmpFile();

        videoRepository.saveInternal(video);

        notificationAsync.createUserNotificationsVideo(subject, now);
        
        Resource<Video> resourceVideo = videoAsssembler.toResource(video);

        return ResponseEntity.created(new URI(resourceVideo.getId().expand().getHref())).body(resourceVideo);
    }

    @DeleteMapping(value = "/api/videos/delete", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('DELETE_VIDEO_PRIVILEGE')")
    public ResponseEntity<?> deleteVideo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("id") Long videoId) {
        // TODO: gestionar tags y errores
        User user = userAuth.getUser();
        Video video = videoRepository.findById(videoId).get();

        if (video.getUploader() != user) {
            // TODO: lanzar excepcion o algo
        }

        s3VideoHandler.deleteFile(video.getUrl());
        s3ImageHandler.deleteFile(video.getThumbnailUrl());

        videoRepository.delete(video);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
