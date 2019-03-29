package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;

    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "fk_category")
    private NotificationCategory category;

    @OneToMany(mappedBy = "notification")
    private Collection<UserIsNotified> users;

}