package com.unicast.unicast_backend.s3handlers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class S3FileHandler {
    @Autowired
    private S3Constants s3Constants;

    private static Integer FILE_KEY_LENGTH = 10;

    private File lastFile;

    abstract protected String getFilePrefix();

    abstract protected String getFileFolder();

    public URI uploadFile(MultipartFile uploadedFile)
            throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {
        File file = File.createTempFile(getFilePrefix(), uploadedFile.getOriginalFilename());
        uploadedFile.transferTo(file);

        final String s3Key = getFileFolder() + "/" + getFilePrefix()
                + RandomStringUtils.randomAlphanumeric(FILE_KEY_LENGTH);
        // TODO: excepcion sin tratar
        s3Constants.s3.putObject(s3Constants.BUCKET_NAME, s3Key, file);

        lastFile = file;
        return new URI(s3Constants.s3.getResourceUrl(s3Constants.BUCKET_NAME, s3Key));
    }

    public void deleteFile(String fileName) {
        System.out.println(fileName);
        s3Constants.s3.deleteObject(s3Constants.BUCKET_NAME, fileName);
    }
 
    public File getLastUploadedTmpFile() {
        return lastFile;
    }

    public void deleteLastUploadedTmpFile() {
        lastFile.delete();
    }
}
