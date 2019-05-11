package com.unicast.unicast_backend.s3handlers;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Constants {

    public final String CLIENT_REGION;
    public final String BUCKET_NAME;
    public final AmazonS3Client s3;


    public S3Constants(@Value("${unicast.s3.client_region}") String clientRegion,
            @Value("${unicast.s3.bucket_name}") String bucketName) {
        CLIENT_REGION = clientRegion;
        BUCKET_NAME = bucketName;
        s3 = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
    }

}