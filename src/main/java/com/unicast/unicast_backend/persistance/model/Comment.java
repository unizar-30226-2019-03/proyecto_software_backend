package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Timestamp timestamp;

    @Column(name="secs_from_beg")
    private Integer secondsFromBeginning;
    
    @ManyToOne
    @JoinColumn(name = "fk_video")
    private Video video;
        
    @ManyToOne
    @JoinColumn(name = "fk_app_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_comment")
    private Comment commentRepliedTo;
}
