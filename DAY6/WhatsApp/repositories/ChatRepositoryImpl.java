package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.ChatConversation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatRepositoryImpl implements ChatRepository {
    private Map<String, ChatConversation> chatStore;
    private Map<String, Set<String>> userChatsIndex;
    private Map<String, String> userPairIndex;
    
    private static ChatRepositoryImpl instance;
    
    private ChatRepositoryImpl() {
        this.chatStore = new HashMap<>();
        this.userChatsIndex = new HashMap<>();
        this.userPairIndex = new HashMap<>();
    }
    
    public static ChatRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ChatRepositoryImpl();
        }
        return instance;
    }
    
    public ChatConversation save(ChatConversation chat) {
        if (chat == null || chat.getChatID() == null) {
            return null;
        }
        if (chat.getUser1() == null || chat.getUser2() == null) {
            return null;
        }
        
        chatStore.put(chat.getChatID(), chat);
        
        String user1Id = chat.getUser1().getUserID();
        String user2Id = chat.getUser2().getUserID();
        
        userChatsIndex.putIfAbsent(user1Id, new HashSet<>());
        userChatsIndex.get(user1Id).add(chat.getChatID());
        
        userChatsIndex.putIfAbsent(user2Id, new HashSet<>());
        userChatsIndex.get(user2Id).add(chat.getChatID());
        
        String pairKey = createPairKey(user1Id, user2Id);
        userPairIndex.put(pairKey, chat.getChatID());
        
        return chat;
    }
    
    public ChatConversation findById(String chatId) {
        return chatStore.get(chatId);
    }
    
    public ChatConversation findByUsers(String userId1, String userId2) {
        String pairKey = createPairKey(userId1, userId2);
        String chatId = userPairIndex.get(pairKey);
        return chatId != null ? chatStore.get(chatId) : null;
    }
    
    public List<ChatConversation> findByUserId(String userId) {
        List<ChatConversation> userChats = new ArrayList<>();
        Set<String> chatIds = userChatsIndex.getOrDefault(userId, new HashSet<>());
        for (String chatId : chatIds) {
            ChatConversation chat = chatStore.get(chatId);
            if (chat != null) {
                userChats.add(chat);
            }
        }
        return userChats;
    }
    
    public ChatConversation update(ChatConversation chat) {
        if (chat == null || chat.getChatID() == null) {
            return null;
        }
        if (!chatStore.containsKey(chat.getChatID())) {
            return null;
        }
        chatStore.put(chat.getChatID(), chat);
        return chat;
    }
    
    public void delete(String chatId) {
        ChatConversation chat = chatStore.get(chatId);
        if (chat != null) {
            chatStore.remove(chatId);
            
            String user1Id = chat.getUser1().getUserID();
            String user2Id = chat.getUser2().getUserID();
            
            Set<String> user1Chats = userChatsIndex.get(user1Id);
            if (user1Chats != null) {
                user1Chats.remove(chatId);
            }
            
            Set<String> user2Chats = userChatsIndex.get(user2Id);
            if (user2Chats != null) {
                user2Chats.remove(chatId);
            }
            
            String pairKey = createPairKey(user1Id, user2Id);
            userPairIndex.remove(pairKey);
        }
    }
    
    private String createPairKey(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + ":" + userId2;
        } else {
            return userId2 + ":" + userId1;
        }
    }
}
