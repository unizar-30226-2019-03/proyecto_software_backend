package com.unicast.unicast_backend.persistance.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;

import java.util.Set;

import lombok.Data;

@Data
@Entity
@Table(name = "video_tag")
public class VideoTag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    Set<Video> videos;
    
}
