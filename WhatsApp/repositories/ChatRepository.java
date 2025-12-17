package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.ChatConversation;
import java.util.List;

public interface ChatRepository {
    ChatConversation save(ChatConversation chat);
    ChatConversation findById(String chatId);
    ChatConversation findByUsers(String userId1, String userId2);
    List<ChatConversation> findByUserId(String userId);
    ChatConversation update(ChatConversation chat);
    void delete(String chatId);
}
