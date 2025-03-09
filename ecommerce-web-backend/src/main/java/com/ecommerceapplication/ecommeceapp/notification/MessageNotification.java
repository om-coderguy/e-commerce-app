package com.ecommerceapplication.ecommeceapp.notification;

import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageNotification implements Notification{

    static final Logger LOGGER= LoggerFactory.getLogger(MessageNotification.class);

    @Override
    public void send(String sms,String data){
        LOGGER.info("Message Notification - : Order placed successfully \t Mobile No. - "+data);
    }

}
