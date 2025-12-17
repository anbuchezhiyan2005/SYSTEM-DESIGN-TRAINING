package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.UserRole;
import java.time.LocalDateTime;

public class GroupMembership {
    private String userID;
    private String groupID;
    private UserRole role;
    private LocalDateTime joinedAt;
    private String addedBy;
    private boolean isMuted;
    private boolean isArchived;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
    }

    public void demoteToMember() {
        this.role = UserRole.MEMBER;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
