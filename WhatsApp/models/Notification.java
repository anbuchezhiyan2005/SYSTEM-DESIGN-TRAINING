package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.NotificationType;
import java.time.LocalDateTime;

public class Notification {
    private String notificationID;
    private NotificationType notificationType;
    private String notificationDescription;
    private User notificationSender;
    private LocalDateTime notificationDateAndTime;
    private boolean isRead;
    private LocalDateTime readAt;
    private String relatedEntityId;

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public User getNotificationSender() {
        return notificationSender;
    }

    public void setNotificationSender(User notificationSender) {
        this.notificationSender = notificationSender;
    }

    public LocalDateTime getNotificationDateAndTime() {
        return notificationDateAndTime;
    }

    public void setNotificationDateAndTime(LocalDateTime notificationDateAndTime) {
        this.notificationDateAndTime = notificationDateAndTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

}
