package com.unicast.unicast_backend.persistance.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class VoteKey implements Serializable{
	
	@Column(name = "fk_video")
    private Long videoId;
	
	@Column(name = "fk_app_user")
    private Long userId;
	
}