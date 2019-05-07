package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "degree")
public class Degree {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "degree")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "degree_university",
		joinColumns = @JoinColumn(
			name = "fk_degree", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				name = "fk_university", referencedColumnName = "id"))
    private Collection<University> universities;
}
