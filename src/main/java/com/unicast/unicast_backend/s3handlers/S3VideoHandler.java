package com.unicast.unicast_backend.s3handlers;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component
public final class S3VideoHandler {
    @Autowired
    private S3Constants s3Constants;

    private static final Integer VIDEO_KEY_LENGTH = 10;
    private static final String VIDEO_FOLDER = "videos";

    public URI uploadVideo(MultipartFile uploadedVideo)
            throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {
        File videoFile = File.createTempFile("vid", uploadedVideo.getOriginalFilename());
        uploadedVideo.transferTo(videoFile);

        final AmazonS3Client s3 = (AmazonS3Client) AmazonS3ClientBuilder.defaultClient();
        final String s3Key = VIDEO_FOLDER + "/vid" + RandomStringUtils.randomAlphanumeric(VIDEO_KEY_LENGTH);
        // TODO: excepcion sin tratar
        s3.putObject(s3Constants.BUCKET_NAME, s3Key, videoFile);

        return new URI(s3.getResourceUrl(s3Constants.BUCKET_NAME, s3Key));
    }
}
