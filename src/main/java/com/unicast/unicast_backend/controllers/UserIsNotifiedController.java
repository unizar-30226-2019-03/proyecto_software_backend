package com.unicast.unicast_backend.controllers;

import com.unicast.unicast_backend.assemblers.UserIsNotifiedResourceAssembler;
import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;
import com.unicast.unicast_backend.persistance.repository.UserIsNotifiedRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserIsNotifiedController {

    private final UserIsNotifiedRepository userIsNotifiedRepository;

    private final UserIsNotifiedResourceAssembler userIsNotifiedAssembler;

    @Autowired
    public UserIsNotifiedController(UserIsNotifiedRepository userIsNotifiedRepository,
            UserIsNotifiedResourceAssembler userIsNotifiedAssembler) {
        this.userIsNotifiedRepository = userIsNotifiedRepository;
        this.userIsNotifiedAssembler = userIsNotifiedAssembler;
    }

    @PatchMapping("/api/usersAreNotified/check")
    public ResponseEntity<?> checkNotification(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("notification_id") Long notificationId) {
        UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
        userIsNotifiedKey.setNotificationId(notificationId);
        userIsNotifiedKey.setUserId(userAuth.getUser().getId());

        UserIsNotified userIsNotified = userIsNotifiedRepository.findById(userIsNotifiedKey).get();

        userIsNotified.setChecked(true);

        userIsNotifiedRepository.save(userIsNotified);

        Resource<UserIsNotified> resourceUserIsNotified = userIsNotifiedAssembler.toResource(userIsNotified);

        return ResponseEntity.ok(resourceUserIsNotified);
    }
}