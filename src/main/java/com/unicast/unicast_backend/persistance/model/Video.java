package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

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
@Table(name = "video")
public class Video {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private Timestamp timestamp;
    
    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private Subject subject;
    
}
