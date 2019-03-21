package com.unicast.unicast_backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import lombok.Data;

@Data
@Embeddable
public class UserIsNotifiedKey implements Serializable{
	
	@Column(name = "fk_notification")
    Long notificationId;
	
	@Column(name = "fk_app_user")
    Long userId;
	
}
