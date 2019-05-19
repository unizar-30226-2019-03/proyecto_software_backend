package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Formula;

import lombok.Data;

@Data
@Entity
@Table(name = "reproduction_list")
public class ReproductionList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @ManyToOne
    @JoinColumn(name = "fk_app_user")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "list")
    private Collection<Contains> videoList;

    @Formula("(select count(*) from video_list vl where vl.fk_list = id)")
    private Integer numVideos;
    
    @Formula("(select v.thumbnail_url from video_list vl join video v on vl.fk_video = v.id where vl.fk_list = id and vl.position = 1)")
    private URI thumbnail;
}
