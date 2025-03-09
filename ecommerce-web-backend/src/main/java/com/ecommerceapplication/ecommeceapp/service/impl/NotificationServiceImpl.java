package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.constant.NotificationType;
import com.ecommerceapplication.ecommeceapp.exception.NotificationException;
import com.ecommerceapplication.ecommeceapp.notification.Notification;
import com.ecommerceapplication.ecommeceapp.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl  implements NotificationService {

    @Override
    public void send(String template,Long mobileNo, String emailData ) {
      Notification notificationObject= Notification.getNotificationObject(NotificationType.SMS);
      if(notificationObject==null){
          throw new NotificationException("Error while creating email notification object");
      }
      notificationObject.send(template,Long.toString(mobileNo));
      notificationObject= Notification.getNotificationObject(NotificationType.EMAIL);
      if(notificationObject==null){
          throw new NotificationException("Error while creating sms notification object");
      }
      notificationObject.send(template,emailData);
    }

    @Override
    public void send(String template, String contact, NotificationType type) {
        Notification notificationObject= Notification.getNotificationObject(type);
        if(notificationObject==null){
            throw new NotificationException("Error while creating notification object");
        }
        notificationObject.send(template,contact);

    }
}
