package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.GroupConversation;

public interface GroupConversationRepository {
    GroupConversation save(GroupConversation conversation);
    GroupConversation findByGroupId(String groupId);
    GroupConversation update(GroupConversation conversation);
    void delete(String groupId);
}
