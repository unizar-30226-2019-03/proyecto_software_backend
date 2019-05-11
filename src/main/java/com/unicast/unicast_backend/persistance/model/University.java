package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "university")
public class University {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "uni_photo")
	private URI photo;

    @OneToMany(mappedBy = "university")
    private Collection<User> users;
    
    @JsonIgnore
    @OneToMany(mappedBy = "university")
    private Collection<Subject> subjects;

    @ManyToMany(mappedBy = "universities")
    private Collection<Degree> degrees;

}