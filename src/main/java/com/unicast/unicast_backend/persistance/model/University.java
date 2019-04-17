package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @OneToMany(mappedBy = "university")
    private Collection<User> users;
    
    @OneToMany(mappedBy = "university")
    private Collection<Subject> subjects;

	public void setName(String string) {
        this.name = string;
    }
    
    @JsonIgnore
    public Collection<Subject> getSubjects(){
        return this.subjects;
    }
}
