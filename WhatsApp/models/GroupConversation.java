package DAY6.WhatsApp.models;

import java.time.LocalDateTime;
import java.util.Map;

public class GroupConversation {
    private String groupID;
    private Map<LocalDateTime, Message> groupMessagesMap;
    private LocalDateTime createdAt;
    private Message lastMessage;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Map<LocalDateTime, Message> getGroupMessagesMap() {
        return groupMessagesMap;
    }

    public void setGroupMessagesMap(Map<LocalDateTime, Message> groupMessagesMap) {
        this.groupMessagesMap = groupMessagesMap;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void addMessage(Message message) {
        if (message != null && message.getMessageTime() != null) {
            groupMessagesMap.put(message.getMessageTime(), message);
            this.lastMessage = message;
        }
    }

}
