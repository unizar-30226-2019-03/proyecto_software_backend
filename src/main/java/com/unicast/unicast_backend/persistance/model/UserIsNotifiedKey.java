package com.unicast.unicast_backend.persistance.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class UserIsNotifiedKey implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Column(name = "fk_notification")
    private Long notificationId;
	
	@Column(name = "fk_app_user")
    private Long userId;
	
}


