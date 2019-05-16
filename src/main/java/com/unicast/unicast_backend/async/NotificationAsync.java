package com.unicast.unicast_backend.async;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.Notification;
import com.unicast.unicast_backend.persistance.model.NotificationCategory;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;
import com.unicast.unicast_backend.persistance.repository.NotificationCategoryRepository;
import com.unicast.unicast_backend.persistance.repository.NotificationRepository;
import com.unicast.unicast_backend.persistance.repository.UserIsNotifiedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NotificationAsync {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserIsNotifiedRepository userIsNotifiedRepository;

    @Autowired
    private NotificationCategoryRepository notificationCatRepository;

    @Transactional
    @Async
    public CompletableFuture<Notification> createUserNotificationsVideo(Subject subject, Timestamp now) {
        NotificationCategory notificationCat = notificationCatRepository.findByName("videos").get();

        Notification notification = new Notification();
        notification.setText("Notificacion de video");
        notification.setTimestamp(now);
        notification.setCategory(notificationCat);

        notificationRepository.save(notification);

        List<UserIsNotified> usersNotifications = new ArrayList<>();

        for (User user : subject.getFollowers()) {
            UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
            userIsNotifiedKey.setNotificationId(notification.getId());
            userIsNotifiedKey.setUserId(user.getId());
            UserIsNotified userIsNotified = new UserIsNotified();
            userIsNotified.setId(userIsNotifiedKey);
            userIsNotified.setChecked(false);
            usersNotifications.add(userIsNotified);
        }
        userIsNotifiedRepository.saveAll(usersNotifications);

        return CompletableFuture.completedFuture(notification);
    }

    @Transactional
    @Async
    public CompletableFuture<Notification> createUserNotificationsMessage(Message message, Timestamp now) {
        NotificationCategory notificationCat = notificationCatRepository.findByName("messages").get();

        Notification notification = new Notification();
        notification.setText("Notificacion de mensaje");
        notification.setTimestamp(now);
        notification.setCategory(notificationCat);

        notificationRepository.save(notification);

        UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
        userIsNotifiedKey.setNotificationId(notification.getId());
        userIsNotifiedKey.setUserId(message.getReceiver().getId());
        UserIsNotified userIsNotified = new UserIsNotified();
        userIsNotified.setId(userIsNotifiedKey);
        userIsNotified.setChecked(false);
        userIsNotifiedRepository.save(userIsNotified);

        return CompletableFuture.completedFuture(notification);
    }
    
}
