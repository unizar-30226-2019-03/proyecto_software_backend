package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "subject")
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;

    // TODO: mirar lo de los roles
    @ManyToMany(mappedBy = "subjects")
    private Collection<User> users;

    @OneToMany(mappedBy = "subject")
    private Collection<Video> videos;
}
