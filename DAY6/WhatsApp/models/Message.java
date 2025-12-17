package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.MessageStatus;
import DAY6.WhatsApp.enums.MessageType;
import java.time.LocalDateTime;

public class Message {
    private String messageID;
    private String messageContent;
    private User sender;
    private User receiver;
    private LocalDateTime messageTime;
    private MessageType messageType;
    private MessageStatus messageStatus;
    private String mediaUrl;

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }
    public LocalDateTime getMessageTime() { return messageTime; }
    public void setMessageTime(LocalDateTime messageTime) { this.messageTime = messageTime; }
    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    public MessageStatus getMessageStatus() { return messageStatus; }
    public void setMessageStatus(MessageStatus messageStatus) { this.messageStatus = messageStatus; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public void send() {
        this.messageTime = LocalDateTime.now();
        this.messageStatus = MessageStatus.SENT;
    }

    public void attachMedia(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void markAsDelivered() {
        if (this.messageStatus == MessageStatus.SENT) {
            this.messageStatus = MessageStatus.DELIVERED;
        }
    }

    public void markAsRead() {
        if (this.messageStatus == MessageStatus.DELIVERED || this.messageStatus == MessageStatus.SENT) {
            this.messageStatus = MessageStatus.READ;
        }
    }
}
