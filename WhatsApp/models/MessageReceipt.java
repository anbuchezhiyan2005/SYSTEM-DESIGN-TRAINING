package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.MessageStatus;
import java.time.LocalDateTime;

public class MessageReceipt {
    private String messageID;
    private String userID;
    private MessageStatus status;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
        this.readAt = LocalDateTime.now();
    }
}
