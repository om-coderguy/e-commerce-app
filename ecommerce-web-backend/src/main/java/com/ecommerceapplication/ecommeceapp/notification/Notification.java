package com.ecommerceapplication.ecommeceapp.notification;

import com.ecommerceapplication.ecommeceapp.constant.NotificationType;
import com.ecommerceapplication.ecommeceapp.service.NotificationService;
import lombok.*;

public interface Notification {

    public void send(String message,String data);

    static Notification getNotificationObject(NotificationType type) {
        switch (type){
            case SMS: return new MessageNotification();

            case EMAIL:return new EmailNotification();

            default: return null;
        }
    }
}
