package com.unicast.unicast_backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import lombok.Data;

@Data
@Embeddable
public class Notification_App_User_Key implements Serializable{
	
	@Column(name = "fk_notification")
    Long notification_id;
	
	@Column(name = "fk_app_user")
    Long app_user_id;
	
}
