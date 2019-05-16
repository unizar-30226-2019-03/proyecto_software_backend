package com.unicast.unicast_backend.s3handlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// TODO: comprobar que el fichero subido sea una imagen

@Component
public final class S3ImageHandler extends S3FileHandler {
    @Override
    protected String getFilePrefix() {
        return "img";
    }

    @Override
    protected String getFileFolder() {
        return "images";
    }

    @Override
    public URI uploadFile(MultipartFile uploadedFile)
        throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {

        
        return super.uploadFile(uploadedFile);
    }

}
