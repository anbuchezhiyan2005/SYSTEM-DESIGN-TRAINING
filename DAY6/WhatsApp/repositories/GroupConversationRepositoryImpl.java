package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.GroupConversation;
import java.util.HashMap;
import java.util.Map;

public class GroupConversationRepositoryImpl implements GroupConversationRepository {
    private Map<String, GroupConversation> conversationStore;
    private static GroupConversationRepositoryImpl instance;
    
    private GroupConversationRepositoryImpl() {
        this.conversationStore = new HashMap<>();
    }
    
    public static GroupConversationRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupConversationRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public GroupConversation save(GroupConversation conversation) {
        if (conversation == null || conversation.getGroupID() == null) {
            return null;
        }
        conversationStore.put(conversation.getGroupID(), conversation);
        return conversation;
    }
    
    @Override
    public GroupConversation findByGroupId(String groupId) {
        return conversationStore.get(groupId);
    }
    
    @Override
    public GroupConversation update(GroupConversation conversation) {
        if (conversation == null || conversation.getGroupID() == null) {
            return null;
        }
        if (!conversationStore.containsKey(conversation.getGroupID())) {
            return null;
        }
        conversationStore.put(conversation.getGroupID(), conversation);
        return conversation;
    }
    
    @Override
    public void delete(String groupId) {
        conversationStore.remove(groupId);
    }
}
