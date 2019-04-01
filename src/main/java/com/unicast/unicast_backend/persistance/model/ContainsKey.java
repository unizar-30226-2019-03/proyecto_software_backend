package com.unicast.unicast_backend.persistance.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ContainsKey implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Column(name = "fk_video")
    private Long videoId;
	
	@Column(name = "fk_list")
    private Long listId;
	
}