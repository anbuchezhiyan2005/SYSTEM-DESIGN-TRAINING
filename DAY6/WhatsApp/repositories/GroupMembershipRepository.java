package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.GroupMembership;
import java.util.List;

public interface GroupMembershipRepository {
    GroupMembership save(GroupMembership membership);
    GroupMembership findByUserAndGroup(String userId, String groupId);
    List<GroupMembership> findByUserId(String userId);
    List<GroupMembership> findByGroupId(String groupId);
    GroupMembership update(GroupMembership membership);
    void delete(String userId, String groupId);
}
