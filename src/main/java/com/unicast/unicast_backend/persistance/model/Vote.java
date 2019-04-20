package com.unicast.unicast_backend.persistance.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "app_user_video_vote")
public class Vote {
	
	@EmbeddedId
    private VoteKey id;
	
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    private Video video;
	
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    private User user;
	
    private int clarity;
    
    private int quality;

    private int likes;

    private int dislikes;	
}
