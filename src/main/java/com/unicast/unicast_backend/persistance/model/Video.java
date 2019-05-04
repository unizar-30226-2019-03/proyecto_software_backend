package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import lombok.Data;

@Data
@Entity
@Table(name = "video")
public class Video {

    // TODO: meter quien ha subido el video

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Timestamp timestamp;

    private URI url;

    private URI thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "fk_uploader")
    private User uploader;

    @OneToMany(mappedBy = "video")
    private Collection<Comment> comments;

    @OneToMany(mappedBy = "video")
    private Collection<Vote> votes;

    @ManyToMany
    @JoinTable(name = "video_video_tag", joinColumns = @JoinColumn(name = "fk_video"), inverseJoinColumns = @JoinColumn(name = "fk_video_tag"))
    private Collection<VideoTag> tags;

    @Formula("(select avg((v.quality + v.clarity + v.suitability) / 3.0) from video vid join app_user_video_vote v on vid.id = v.fk_video)")
    private Float score;

}
