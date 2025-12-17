package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.Group;
import DAY6.WhatsApp.models.GroupConversation;
import DAY6.WhatsApp.models.GroupMembership;
import DAY6.WhatsApp.models.Message;
import DAY6.WhatsApp.models.User;
import DAY6.WhatsApp.enums.MessageType;
import DAY6.WhatsApp.enums.UserRole;
import DAY6.WhatsApp.repositories.GroupRepository;
import DAY6.WhatsApp.repositories.GroupRepositoryImpl;
import DAY6.WhatsApp.repositories.GroupMembershipRepository;
import DAY6.WhatsApp.repositories.GroupMembershipRepositoryImpl;
import DAY6.WhatsApp.repositories.GroupConversationRepository;
import DAY6.WhatsApp.repositories.GroupConversationRepositoryImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupService {
    private static GroupService instance;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository membershipRepository;
    private final GroupConversationRepository conversationRepository;
    private final UserService userService;
    
    private GroupService(GroupRepository groupRepository, 
                        GroupMembershipRepository membershipRepository,
                        GroupConversationRepository conversationRepository,
                        UserService userService) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }
    
    public static GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService(
                GroupRepositoryImpl.getInstance(),
                GroupMembershipRepositoryImpl.getInstance(),
                GroupConversationRepositoryImpl.getInstance(),
                UserService.getInstance()
            );
        }
        return instance;
    }
    
    public Group createGroup(String creatorId, String groupName, String description) {
        if (creatorId == null || groupName == null || groupName.trim().isEmpty()) {
            return null;
        }
        
        User creator = userService.getUserById(creatorId);
        
        Group group = new Group();
        group.setGroupID(UUID.randomUUID().toString());
        group.setGroupName(groupName);
        group.setGroupDescription(description != null ? description : "");
        group.setCreatedBy(creator);
        group.setCreatedAt(LocalDateTime.now());
        group.setGroupMembersRoleMap(new HashMap<>());
        group.getGroupMembersRoleMap().put(creator, UserRole.ADMIN);
        
        Group savedGroup = groupRepository.save(group);
        
        GroupMembership membership = new GroupMembership();
        membership.setUserID(creatorId);
        membership.setGroupID(group.getGroupID());
        membership.setRole(UserRole.ADMIN);
        membership.setJoinedAt(LocalDateTime.now());
        membership.setAddedBy(creatorId);
        membership.setMuted(false);
        membership.setArchived(false);
        membershipRepository.save(membership);
        
        GroupConversation conversation = new GroupConversation();
        conversation.setGroupID(group.getGroupID());
        conversation.setGroupMessagesMap(new HashMap<>());
        conversation.setCreatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
        
        return savedGroup;
    }
    
    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId);
    }
    
    public List<Group> getGroupsForUser(String userId) {
        List<GroupMembership> memberships = membershipRepository.findByUserId(userId);
        List<Group> groups = new ArrayList<>();
        for (GroupMembership membership : memberships) {
            Group group = groupRepository.findById(membership.getGroupID());
            if (group != null) {
                groups.add(group);
            }
        }
        return groups;
    }
    
    public boolean addMember(String groupId, String userId, String addedById) {
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        GroupMembership adderMembership = membershipRepository.findByUserAndGroup(addedById, groupId);
        if (adderMembership == null || !adderMembership.isAdmin()) {
            return false;
        }
        
        GroupMembership existing = membershipRepository.findByUserAndGroup(userId, groupId);
        if (existing != null) return false;
        
        User user = userService.getUserById(userId);
        
        GroupMembership membership = new GroupMembership();
        membership.setUserID(userId);
        membership.setGroupID(groupId);
        membership.setRole(UserRole.MEMBER);
        membership.setJoinedAt(LocalDateTime.now());
        membership.setAddedBy(addedById);
        membership.setMuted(false);
        membership.setArchived(false);
        membershipRepository.save(membership);
        
        group.getGroupMembersRoleMap().put(user, UserRole.MEMBER);
        groupRepository.update(group);
        
        return true;
    }
    
    public boolean removeMember(String groupId, String userId, String removedById) {
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        GroupMembership removerMembership = membershipRepository.findByUserAndGroup(removedById, groupId);
        if (removerMembership == null || !removerMembership.isAdmin()) {
            return false;
        }
        
        GroupMembership targetMembership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (targetMembership == null) return false;
        
        membershipRepository.delete(userId, groupId);
        
        User user = userService.getUserById(userId);
        group.getGroupMembersRoleMap().remove(user);
        groupRepository.update(group);
        
        return true;
    }
    
    public boolean promoteToAdmin(String groupId, String userId, String promoterId) {
        GroupMembership promoterMembership = membershipRepository.findByUserAndGroup(promoterId, groupId);
        if (promoterMembership == null || !promoterMembership.isAdmin()) {
            return false;
        }
        
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.promoteToAdmin();
        membershipRepository.update(membership);
        
        Group group = groupRepository.findById(groupId);
        User user = userService.getUserById(userId);
        group.getGroupMembersRoleMap().put(user, UserRole.ADMIN);
        groupRepository.update(group);
        
        return true;
    }
    
    public boolean demoteToMember(String groupId, String userId, String demoterId) {
        GroupMembership demoterMembership = membershipRepository.findByUserAndGroup(demoterId, groupId);
        if (demoterMembership == null || !demoterMembership.isAdmin()) {
            return false;
        }
        
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.demoteToMember();
        membershipRepository.update(membership);
        
        Group group = groupRepository.findById(groupId);
        User user = userService.getUserById(userId);
        group.getGroupMembersRoleMap().put(user, UserRole.MEMBER);
        groupRepository.update(group);
        
        return true;
    }
    
    public boolean updateGroupName(String groupId, String userId, String newName) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null || !membership.isAdmin()) {
            return false;
        }
        
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        group.updateGroupName(newName);
        groupRepository.update(group);
        return true;
    }
    
    public boolean updateGroupDescription(String groupId, String userId, String newDescription) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null || !membership.isAdmin()) {
            return false;
        }
        
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        group.updateGroupDescription(newDescription);
        groupRepository.update(group);
        return true;
    }
    
    public boolean updateGroupIcon(String groupId, String userId, String iconUrl) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null || !membership.isAdmin()) {
            return false;
        }
        
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        group.updateGroupIcon(iconUrl);
        groupRepository.update(group);
        return true;
    }
    
    public Message sendGroupMessage(String groupId, String senderId, String content, MessageType type) {
        Group group = groupRepository.findById(groupId);
        if (group == null) return null;
        
        GroupMembership membership = membershipRepository.findByUserAndGroup(senderId, groupId);
        if (membership == null) return null;
        
        User sender = userService.getUserById(senderId);
        GroupConversation conversation = conversationRepository.findByGroupId(groupId);
        if (conversation == null) return null;
        
        Message message = new Message();
        message.setMessageID(UUID.randomUUID().toString());
        message.setMessageContent(content);
        message.setSender(sender);
        message.setMessageType(type);
        message.send();
        
        conversation.addMessage(message);
        conversationRepository.update(conversation);
        
        return message;
    }
    
    public Message sendGroupMessageWithMedia(String groupId, String senderId, String content, MessageType type, String mediaUrl) {
        Group group = groupRepository.findById(groupId);
        if (group == null) return null;
        
        GroupMembership membership = membershipRepository.findByUserAndGroup(senderId, groupId);
        if (membership == null) return null;
        
        User sender = userService.getUserById(senderId);
        GroupConversation conversation = conversationRepository.findByGroupId(groupId);
        if (conversation == null) return null;
        
        Message message = new Message();
        message.setMessageID(UUID.randomUUID().toString());
        message.setMessageContent(content);
        message.setSender(sender);
        message.setMessageType(type);
        message.attachMedia(mediaUrl);
        message.send();
        
        conversation.addMessage(message);
        conversationRepository.update(conversation);
        
        return message;
    }
    
    public Map<LocalDateTime, Message> getGroupMessages(String groupId, String userId) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return null;
        
        GroupConversation conversation = conversationRepository.findByGroupId(groupId);
        return conversation != null ? conversation.getGroupMessagesMap() : null;
    }
    
    public List<GroupMembership> getGroupMembers(String groupId) {
        return membershipRepository.findByGroupId(groupId);
    }
    
    public boolean deleteGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) return false;
        
        if (!group.getCreatedBy().getUserID().equals(userId)) {
            return false;
        }
        
        List<GroupMembership> memberships = membershipRepository.findByGroupId(groupId);
        for (GroupMembership membership : memberships) {
            membershipRepository.delete(membership.getUserID(), groupId);
        }
        
        conversationRepository.delete(groupId);
        groupRepository.delete(groupId);
        
        return true;
    }
    
    public boolean muteGroup(String groupId, String userId) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.setMuted(true);
        membershipRepository.update(membership);
        return true;
    }
    
    public boolean unmuteGroup(String groupId, String userId) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.setMuted(false);
        membershipRepository.update(membership);
        return true;
    }
    
    public boolean archiveGroup(String groupId, String userId) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.setArchived(true);
        membershipRepository.update(membership);
        return true;
    }
    
    public boolean unarchiveGroup(String groupId, String userId) {
        GroupMembership membership = membershipRepository.findByUserAndGroup(userId, groupId);
        if (membership == null) return false;
        
        membership.setArchived(false);
        membershipRepository.update(membership);
        return true;
    }
}
