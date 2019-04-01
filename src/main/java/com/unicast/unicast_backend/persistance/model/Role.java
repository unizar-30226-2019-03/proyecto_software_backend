package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
        name = "role_privilege", 
        joinColumns = @JoinColumn(
          name = "fk_role", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "fk_privilege", referencedColumnName = "id"))
    private Collection<Privilege> privileges;  
}