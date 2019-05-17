package com.unicast.unicast_backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.unicast.unicast_backend.assemblers.CommentResourceAssembler;
import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.CommentRepository;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private final CommentRepository commentRepository;

    private final VideoRepository videoRepository;

    private final CommentResourceAssembler commentAssembler;

    @Autowired
    public CommentController(CommentRepository u, VideoRepository videoRepository,
            CommentResourceAssembler commentAssembler) {
        this.commentRepository = u;
        this.videoRepository = videoRepository;
        this.commentAssembler = commentAssembler;
    }

    @PostMapping(value = "/api/comments", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addNewComment(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("text") String text, @RequestParam("secs_from_beg") Integer secondsFromBeginning,
            @RequestParam("video_id") Long videoId,
            @RequestParam(name = "comment_replied_to_id", required = false) Long commentRepliedToId)
            throws URISyntaxException {
        User user = userAuth.getUser();
        // TODO: comprobar que secs_from_beg sea <= que la duracion del video y sino lance excepcion

        Comment comment = new Comment();

        comment.setText(text);
        comment.setSecondsFromBeginning(secondsFromBeginning);
        comment.setTimestamp(Timestamp.from(Instant.now()));

        Video video = videoRepository.findById(videoId).get();
        comment.setVideo(video);

        if (commentRepliedToId != null) {
            Comment commentRepliedTo = commentRepository.findById(commentRepliedToId).get();
            comment.setCommentRepliedTo(commentRepliedTo);
        }

        comment.setUser(user);

        commentRepository.saveInternal(comment);

        Resource<Comment> resourceComment = commentAssembler.toResource(comment);

        return ResponseEntity.created(new URI(resourceComment.getId().expand().getHref())).body(resourceComment);
    }
}