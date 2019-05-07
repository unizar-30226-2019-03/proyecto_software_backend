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

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import lombok.Data;

@Data
@Entity
@Table(name = "video")
// @NamedQuery(name="Video.getUniversity", query="select u from Subject s join University u on s.university = u where subject = s")
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

    private Integer seconds;

    @ManyToOne
        @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula=@JoinFormula(value="(select u.id from subject s join university u on s.fk_university = u.id where fk_subject = s.id)", referencedColumnName = "id"))
        })
    private University university;

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

    @JsonIgnore
	@OneToMany(mappedBy = "video")
	private Collection<Display> displays;

    @ManyToMany
    @JoinTable(name = "video_video_tag", joinColumns = @JoinColumn(name = "fk_video"), inverseJoinColumns = @JoinColumn(name = "fk_video_tag"))
    private Collection<VideoTag> tags;

    @Formula("(select avg((v.quality + v.clarity + v.suitability) / 3.0) from app_user_video_vote v where id = v.fk_video)")
    private Float score;

}
