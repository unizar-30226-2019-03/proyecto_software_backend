package com.unicast.unicast_backend.s3handlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;
import com.unicast.unicast_backend.exceptions.NotImageException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        if (!MediaType.parseMediaType(uploadedFile.getContentType()).getType().equals("image")) {
            throw new NotImageException("El fichero subido no es una imagen");
        }
        return super.uploadFile(uploadedFile);
    }

}
