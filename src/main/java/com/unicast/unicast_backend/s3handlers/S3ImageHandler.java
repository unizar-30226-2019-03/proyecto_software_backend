package com.unicast.unicast_backend.s3handlers;

import org.springframework.stereotype.Component;

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
}
