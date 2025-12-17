package DAY6.WhatsApp.models;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatConversation {
    private String chatID;
    private User user1;
    private User user2;
    private Map<LocalDateTime, Message> chatMessagesMap;
    private LocalDateTime createdAt;
    private Message lastMessage;

    public String getChatID() { return chatID; }
    public void setChatID(String chatID) { this.chatID = chatID; }
    public User getUser1() { return user1; }
    public void setUser1(User user1) { this.user1 = user1; }
    public User getUser2() { return user2; }
    public void setUser2(User user2) { this.user2 = user2; }
    public Map<LocalDateTime, Message> getChatMessagesMap() { return chatMessagesMap; }
    public void setChatMessagesMap(Map<LocalDateTime, Message> chatMessagesMap) { this.chatMessagesMap = chatMessagesMap; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Message getLastMessage() { return lastMessage; }
    public void setLastMessage(Message lastMessage) { this.lastMessage = lastMessage; }

    public void addMessage(Message message) {
        if (message != null && message.getMessageTime() != null) {
            chatMessagesMap.put(message.getMessageTime(), message);
            this.lastMessage = message;
        }
    }
}