package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;
    
    private boolean isTeacherSender;
    
    private Timestamp timestamp;
    
    
    
    //TODO: Investigar sobre roles para saber como hacer esto bien
    
    /*@ManyToOne
    @JoinColumn(name = "fk_teacher")
    private App_User teacher;
    
    @ManyToOne
    @JoinColumn(name = "fk_alumn")
    private App_User alumn;*/
    
}
