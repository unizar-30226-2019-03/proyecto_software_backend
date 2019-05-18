package com.unicast.unicast_backend.persistance.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NamedQuery;

import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Subject.findRanking",
    query = "select new Subject(sum(v.score) / count(v), s) from Subject s join Video v on s.id = v.subject.id group by s.id order by sum(v.score) / count(v) desc")
@Table(name = "subject")
public class Subject {

    public Subject() {}

    public Subject(Double avgScore, Subject s) {
        this.id = s.id;
        this.name = s.name;
        this.abbreviation = s.abbreviation;
        this.university = s.university;
        this.followers = s.followers;
        this.professors = s.professors;
        this.videos = s.videos;

        this.avgScore = avgScore;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String abbreviation;

    @Transient
    private Double avgScore;

    @ManyToOne
    @JoinColumn(name = "fk_university")
    private University university;

    @ManyToMany
    @JoinTable(name = "app_user_subject", 
        joinColumns = @JoinColumn(name = "fk_subject"), 
        inverseJoinColumns = @JoinColumn(name = "fk_app_user"))
    private Collection<User> followers;

    @ManyToMany
    @JoinTable(name = "app_professor_subject", 
        joinColumns = @JoinColumn(name = "fk_subject"), 
        inverseJoinColumns = @JoinColumn(name = "fk_professor"))
    private Collection<User> professors;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private Collection<Video> videos;
}
