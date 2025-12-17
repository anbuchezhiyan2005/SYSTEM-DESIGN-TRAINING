package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.GroupMembership;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMembershipRepositoryImpl implements GroupMembershipRepository {
    private Map<String, GroupMembership> membershipStore;
    private Map<String, List<String>> userGroupsIndex;
    private Map<String, List<String>> groupMembersIndex;
    private static GroupMembershipRepositoryImpl instance;
    
    private GroupMembershipRepositoryImpl() {
        this.membershipStore = new HashMap<>();
        this.userGroupsIndex = new HashMap<>();
        this.groupMembersIndex = new HashMap<>();
    }
    
    public static GroupMembershipRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupMembershipRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public GroupMembership save(GroupMembership membership) {
        if (membership == null || membership.getUserID() == null || membership.getGroupID() == null) {
            return null;
        }
        
        String key = createKey(membership.getUserID(), membership.getGroupID());
        membershipStore.put(key, membership);
        
        userGroupsIndex.putIfAbsent(membership.getUserID(), new ArrayList<>());
        if (!userGroupsIndex.get(membership.getUserID()).contains(membership.getGroupID())) {
            userGroupsIndex.get(membership.getUserID()).add(membership.getGroupID());
        }
        
        groupMembersIndex.putIfAbsent(membership.getGroupID(), new ArrayList<>());
        if (!groupMembersIndex.get(membership.getGroupID()).contains(membership.getUserID())) {
            groupMembersIndex.get(membership.getGroupID()).add(membership.getUserID());
        }
        
        return membership;
    }
    
    @Override
    public GroupMembership findByUserAndGroup(String userId, String groupId) {
        String key = createKey(userId, groupId);
        return membershipStore.get(key);
    }
    
    @Override
    public List<GroupMembership> findByUserId(String userId) {
        List<GroupMembership> memberships = new ArrayList<>();
        List<String> groupIds = userGroupsIndex.getOrDefault(userId, new ArrayList<>());
        for (String groupId : groupIds) {
            String key = createKey(userId, groupId);
            GroupMembership membership = membershipStore.get(key);
            if (membership != null) {
                memberships.add(membership);
            }
        }
        return memberships;
    }
    
    @Override
    public List<GroupMembership> findByGroupId(String groupId) {
        List<GroupMembership> memberships = new ArrayList<>();
        List<String> userIds = groupMembersIndex.getOrDefault(groupId, new ArrayList<>());
        for (String userId : userIds) {
            String key = createKey(userId, groupId);
            GroupMembership membership = membershipStore.get(key);
            if (membership != null) {
                memberships.add(membership);
            }
        }
        return memberships;
    }
    
    @Override
    public GroupMembership update(GroupMembership membership) {
        if (membership == null || membership.getUserID() == null || membership.getGroupID() == null) {
            return null;
        }
        String key = createKey(membership.getUserID(), membership.getGroupID());
        if (!membershipStore.containsKey(key)) {
            return null;
        }
        membershipStore.put(key, membership);
        return membership;
    }
    
    @Override
    public void delete(String userId, String groupId) {
        String key = createKey(userId, groupId);
        membershipStore.remove(key);
        
        List<String> userGroups = userGroupsIndex.get(userId);
        if (userGroups != null) {
            userGroups.remove(groupId);
        }
        
        List<String> groupMembers = groupMembersIndex.get(groupId);
        if (groupMembers != null) {
            groupMembers.remove(userId);
        }
    }
    
    private String createKey(String userId, String groupId) {
        return userId + ":" + groupId;
    }
}
