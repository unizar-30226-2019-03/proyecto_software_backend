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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Long creatorId;

    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "fk_category")
    private NotificationCategory category;

    @OneToMany(mappedBy = "notification")
    private Collection<UserIsNotified> users;

	@JsonProperty(access = Access.READ_ONLY)
    public String getNotificationCategory() {
        return category.getName();
    }
}
