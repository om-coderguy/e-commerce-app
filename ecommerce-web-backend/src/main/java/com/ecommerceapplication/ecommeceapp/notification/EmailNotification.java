package com.ecommerceapplication.ecommeceapp.notification;

import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class EmailNotification implements Notification{

    static final Logger LOGGER= LoggerFactory.getLogger(EmailNotification.class);

    @Override
    public void send(String email,String data){
        LOGGER.info("Email Notification - :Order placed successfully \t Email - "+data);
    }
}
