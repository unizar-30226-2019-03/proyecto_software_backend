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
@Table(name = "video_list")
public class Contains {
	
	@EmbeddedId
    private ContainsKey id;
	
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    private Video video;
	
	@ManyToOne
    @MapsId("fk_list")
    @JoinColumn(name = "fk_list")
    private ReproductionList list;
	
	private Integer position;
}
