package com.unicast.unicast_backend.configuration;

import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.model.Degree;
import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.Notification;
import com.unicast.unicast_backend.persistance.model.ReproductionList;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.University;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.model.Vote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(User.class);
        config.exposeIdsFor(Video.class);
        config.exposeIdsFor(University.class);
        config.exposeIdsFor(Subject.class);
        config.exposeIdsFor(Degree.class);
        config.exposeIdsFor(Comment.class);
        config.exposeIdsFor(Display.class);
        config.exposeIdsFor(Vote.class);
        config.exposeIdsFor(Message.class);
        config.exposeIdsFor(Notification.class);
        config.exposeIdsFor(ReproductionList.class);
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategies.ANNOTATED);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}