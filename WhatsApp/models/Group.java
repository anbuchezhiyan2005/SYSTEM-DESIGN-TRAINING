package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.UserRole;
import java.time.LocalDateTime;
import java.util.Map;

public class Group {
    private String groupID;
    private String groupName;
    private String groupDescription;
    private Map<User, UserRole> groupMembersRoleMap;
    private User createdBy;
    private LocalDateTime createdAt;
    private String groupIconUrl;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Map<User, UserRole> getGroupMembersRoleMap() {
        return groupMembersRoleMap;
    }

    public void setGroupMembersRoleMap(Map<User, UserRole> groupMembersRoleMap) {
        this.groupMembersRoleMap = groupMembersRoleMap;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupIconUrl() {
        return groupIconUrl;
    }

    public void setGroupIconUrl(String groupIconUrl) {
        this.groupIconUrl = groupIconUrl;
    }

    public UserRole getMemberRole(User user) {
        return groupMembersRoleMap.get(user);
    }

    public boolean isMember(User user) {
        return groupMembersRoleMap.containsKey(user);
    }

    public boolean isAdmin(User user) {
        return groupMembersRoleMap.get(user) == UserRole.ADMIN;
    }

    public void updateGroupName(String newName) {
        this.groupName = newName;
    }

    public void updateGroupDescription(String newDescription) {
        this.groupDescription = newDescription;
    }

    public void updateGroupIcon(String iconUrl) {
        this.groupIconUrl = iconUrl;
    }
}
