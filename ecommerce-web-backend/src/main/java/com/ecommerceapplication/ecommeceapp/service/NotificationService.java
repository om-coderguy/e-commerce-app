package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.constant.NotificationType;
import com.ecommerceapplication.ecommeceapp.entity.Order;

public interface NotificationService {

    public void send(String template,Long mobileNo,String emailData);

    public void send(String template, String contact,NotificationType type);



}
