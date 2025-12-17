package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.ChatConversation;
import DAY6.WhatsApp.models.Message;
import DAY6.WhatsApp.models.User;
import DAY6.WhatsApp.enums.MessageType;
import DAY6.WhatsApp.repositories.ChatRepository;
import DAY6.WhatsApp.repositories.ChatRepositoryImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatService {
    private static ChatService instance;
    private final ChatRepository chatRepository;
    private final UserService userService;
    
    private ChatService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }
    
    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService(ChatRepositoryImpl.getInstance(), UserService.getInstance());
        }
        return instance;
    }
    
    public ChatConversation getOrCreateChat(String userId1, String userId2) {
        if (userId1 == null || userId2 == null) return null;
        if (userId1.equals(userId2)) return null;
        
        ChatConversation existing = chatRepository.findByUsers(userId1, userId2);
        if (existing != null) {
            return existing;
        }
        
        User user1 = userService.getUserById(userId1);
        User user2 = userService.getUserById(userId2);
        
        ChatConversation newChat = new ChatConversation();
        newChat.setChatID(UUID.randomUUID().toString());
        newChat.setUser1(user1);
        newChat.setUser2(user2);
        newChat.setChatMessagesMap(new HashMap<>());
        newChat.setCreatedAt(LocalDateTime.now());
        
        return chatRepository.save(newChat);
    }
    
    public ChatConversation getChatById(String chatId) {
        return chatRepository.findById(chatId);
    }
    
    public ChatConversation getChatBetweenUsers(String userId1, String userId2) {
        return chatRepository.findByUsers(userId1, userId2);
    }
    
    public List<ChatConversation> getChatsForUser(String userId) {
        return chatRepository.findByUserId(userId);
    }
    
    public Message sendMessage(String chatId, String senderId, String content, MessageType type) {
        ChatConversation chat = chatRepository.findById(chatId);
        if (chat == null) return null;
        
        User sender = userService.getUserById(senderId);
        User receiver = getOtherUser(chat, senderId);
        if (receiver == null) return null;
        
        Message message = new Message();
        message.setMessageID(UUID.randomUUID().toString());
        message.setMessageContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageType(type);
        message.send();
        
        chat.addMessage(message);
        chatRepository.update(chat);
        
        return message;
    }
    
    public Message sendMessageWithMedia(String chatId, String senderId, String content, MessageType type, String mediaUrl) {
        ChatConversation chat = chatRepository.findById(chatId);
        if (chat == null) return null;
        
        User sender = userService.getUserById(senderId);
        User receiver = getOtherUser(chat, senderId);
        if (receiver == null) return null;
        
        Message message = new Message();
        message.setMessageID(UUID.randomUUID().toString());
        message.setMessageContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageType(type);
        message.attachMedia(mediaUrl);
        message.send();
        
        chat.addMessage(message);
        chatRepository.update(chat);
        
        return message;
    }
    
    public boolean markMessageAsDelivered(String chatId, LocalDateTime messageTime) {
        ChatConversation chat = chatRepository.findById(chatId);
        if (chat == null) return false;
        
        Message message = chat.getChatMessagesMap().get(messageTime);
        if (message == null) return false;
        
        message.markAsDelivered();
        chatRepository.update(chat);
        return true;
    }
    
    public boolean markMessageAsRead(String chatId, LocalDateTime messageTime) {
        ChatConversation chat = chatRepository.findById(chatId);
        if (chat == null) return false;
        
        Message message = chat.getChatMessagesMap().get(messageTime);
        if (message == null) return false;
        
        message.markAsRead();
        chatRepository.update(chat);
        return true;
    }
    
    public Map<LocalDateTime, Message> getMessages(String chatId) {
        ChatConversation chat = chatRepository.findById(chatId);
        return chat != null ? chat.getChatMessagesMap() : null;
    }
    
    public Message getLastMessage(String chatId) {
        ChatConversation chat = chatRepository.findById(chatId);
        return chat != null ? chat.getLastMessage() : null;
    }
    
    public boolean deleteChat(String chatId) {
        ChatConversation chat = chatRepository.findById(chatId);
        if (chat == null) return false;
        chatRepository.delete(chatId);
        return true;
    }
    
    private User getOtherUser(ChatConversation chat, String userId) {
        if (chat.getUser1().getUserID().equals(userId)) {
            return chat.getUser2();
        } else if (chat.getUser2().getUserID().equals(userId)) {
            return chat.getUser1();
        }
        return null;
    }
}
