package com.unicast.unicast_backend.persistance.model;

import javax.persistence.EmbeddedId;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "notification_app_user")
public class UserIsNotified {
	
	@EmbeddedId
    private UserIsNotifiedKey id;
	
	@ManyToOne
    @MapsId("fk_notification")
    @JoinColumn(name = "fk_notification")
    private Notification notification;
	
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    private User user;
	
	private boolean checked;
	
	
}
