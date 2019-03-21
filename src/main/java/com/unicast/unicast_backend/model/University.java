package com.unicast.unicast_backend.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "university")
public class University {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private Set<App_User> users;
    
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private Set<Subject> subjects;
}
