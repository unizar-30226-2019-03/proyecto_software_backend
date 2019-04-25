// TODO: arreglar, mirar si hace falta o no para gestionar multipart

package com.unicast.unicast_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}