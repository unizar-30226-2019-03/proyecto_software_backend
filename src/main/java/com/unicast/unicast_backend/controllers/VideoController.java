package com.unicast.unicast_backend.controllers;

import java.io.IOException;

// import com.unicast.unicast_backend.s3handlers.S3VideoHandler;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@RepositoryRestController
public class VideoController {

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void uploadVideo(MultipartFile uploadedVideo) throws IllegalStateException, IOException {
        // String s3Path = S3VideoHandler.uploadVideo(uploadedVideo);

    }
}
