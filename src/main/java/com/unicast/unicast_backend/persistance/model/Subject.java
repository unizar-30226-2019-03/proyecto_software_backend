/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

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

/*
 * Entidad asignatura
 */

@Data
@Entity
@NamedQuery(name = "Subject.findRanking",
    query = "select new Subject(sum(v.score) / count(v), s) from Subject s join Video v on s.id = v.subject.id group by s.id order by (sum(v.score) / count(v)) desc nulls last")
@Table(name = "subject")
public class Subject {

    // Constructor de la clase
    public Subject() {}

    // Constructor de la clase en base a su media
    public Subject(Double avgScore, Subject s) {
        this.id = s.id;
        this.name = s.name;
        this.abbreviation = s.abbreviation;
        this.university = s.university;
        this.followers = s.followers;
        this.professors = s.professors;
        this.videos = s.videos;

        if (avgScore == null) {
            this.avgScore = 0.0;
        } else {
            this.avgScore = avgScore;
        }
    }

    // Identificador de la asignatura
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la asignatura
    private String name;

    // Abreviatura de la asignatura
    private String abbreviation;

    // Puntuacion de la asignatura
    @Transient
    private Double avgScore;

    // Relacion N:1 con universidad
    @ManyToOne
    @JoinColumn(name = "fk_university")
    // Universidad donde se imparte la asignatura
    private University university;

    // Relacion N:M con usuarios seguidores
    @ManyToMany
    @JoinTable(name = "app_user_subject", 
        joinColumns = @JoinColumn(name = "fk_subject"), 
        inverseJoinColumns = @JoinColumn(name = "fk_app_user"))
    // Coleccion de usuarios seguidores de la asignatura
    private Collection<User> followers;

    // Relacion N:M con usuarios profesores que la imparten
    @ManyToMany
    @JoinTable(name = "app_professor_subject", 
        joinColumns = @JoinColumn(name = "fk_subject"), 
        inverseJoinColumns = @JoinColumn(name = "fk_professor"))
    // Lista de profesores que imparten la asignatura
    private Collection<User> professors;

    // Relacion 1:N con videos
    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    // Coleccion de videos de la asignatura
    private Collection<Video> videos;
}
