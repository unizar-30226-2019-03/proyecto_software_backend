package com.unicast.unicast_backend.s3handlers;

import org.springframework.stereotype.Component;


@Component
public final class S3VideoHandler extends S3FileHandler {
    @Override
    protected String getFilePrefix() {
        return "vid";
    }

    @Override
    protected String getFileFolder() {
        return "videos";
    }
}
