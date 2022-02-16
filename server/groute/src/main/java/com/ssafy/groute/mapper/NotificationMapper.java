package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Notification;

import java.util.List;

public interface NotificationMapper {
    void insertNotification(Notification notification) throws Exception;
    Notification selectNotification(int id) throws Exception;
    List<Notification> selectNotificationByUserId(String userId) throws Exception;
    void deleteNotification(int id) throws Exception;
    void updateNotification(Notification notification) throws Exception;
    void deleteNotificationByUserId(String userId) throws Exception;
}
