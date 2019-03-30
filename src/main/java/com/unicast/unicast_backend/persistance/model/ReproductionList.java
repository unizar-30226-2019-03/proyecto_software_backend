package com.unicast.unicast_backend.persistance.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import lombok.Data;

@Data
@Entity
@Table(name = "reproduction_list")
public class ReproductionList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    
    @ManyToOne
    @JoinColumn(name = "fk_app_user")
    private User user;

    @OneToMany(mappedBy = "list")
    private Set<Contains> videoList;
    
}
