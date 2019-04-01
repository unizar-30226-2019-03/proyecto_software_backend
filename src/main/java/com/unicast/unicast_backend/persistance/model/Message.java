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
@Table(name = "message")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    
    private Timestamp timestamp;
    
    @ManyToOne
    @JoinColumn(name = "fk_receiver")
    private User receiver;
    
    @ManyToOne
    @JoinColumn(name = "fk_sender")
    private User sender;
    
}
