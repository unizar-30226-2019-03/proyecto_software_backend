package com.unicast.unicast_backend.s3handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class S3VideoHandler {
    @Autowired
    private static S3Constants s3Constants;

    public static String uploadVideo(MultipartFile uploadedVideo) throws IllegalStateException, IOException {
        File file = new File(uploadedVideo.getOriginalFilename());
        uploadedVideo.transferTo(file);


        // TODO: subirlo y tal a s3
}
