package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import java.net.URI;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import java.util.Set;
import java.util.Collection;

import lombok.Data;

@Data
@Entity
@Table(name = "video")
public class Video {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private Timestamp timestamp;
    
    private URI s3Path;
    
    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private Subject subject;

    @OneToMany(mappedBy = "video")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "video")
    private Set<Contains> videoLists;

    @OneToMany(mappedBy = "video")
    private Set<Display> setDisplays;

    @OneToMany(mappedBy = "video")
    private Set<Vote> votes;
    
    @ManyToMany
	@JoinTable(
		name = "video_video_tag", 
		joinColumns = @JoinColumn(name = "fk_video"), 
		inverseJoinColumns = @JoinColumn(name = "fk_video_tag"))
	private Collection<VideoTag> tags; 
}
