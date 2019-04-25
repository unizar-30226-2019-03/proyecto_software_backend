package com.unicast.unicast_backend.s3handlers;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public abstract class S3FileHandler {
    @Autowired
    private S3Constants s3Constants;

    private static Integer FILE_KEY_LENGTH = 10;

    abstract protected String getFilePrefix();

    abstract protected String getFileFolder();

    public URI uploadFile(MultipartFile uploadedFile)
            throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {
        File file = File.createTempFile(getFilePrefix(), uploadedFile.getOriginalFilename());
        uploadedFile.transferTo(file);

        final AmazonS3Client s3 = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(s3Constants.CLIENT_REGION).build();
        final String s3Key = getFileFolder() + "/" + getFilePrefix()
                + RandomStringUtils.randomAlphanumeric(FILE_KEY_LENGTH);
        // TODO: excepcion sin tratar
        s3.putObject(s3Constants.BUCKET_NAME, s3Key, file);

        return new URI(s3.getResourceUrl(s3Constants.BUCKET_NAME, s3Key));
    }
}
