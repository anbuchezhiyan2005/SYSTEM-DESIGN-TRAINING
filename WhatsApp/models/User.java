package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.OnlineStatus;
import java.time.LocalDateTime;
import java.util.Set;

public class User {
    private String userID;
    private String userName;
    private String userMobileNumber;
    private String userDescription;
    private String userStatus;
    private String userProfilePictureUrl;
    private Set<Contact> userContactList;
    private Set<Group> userGroupList;
    private LocalDateTime lastSeen;
    private OnlineStatus onlineStatus;

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserMobileNumber() { return userMobileNumber; }
    public void setUserMobileNumber(String userMobileNumber) { this.userMobileNumber = userMobileNumber; }
    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
    public String getUserStatus() { return userStatus; }
    public void setUserStatus(String userStatus) { this.userStatus = userStatus; }
    public String getUserProfilePictureUrl() { return userProfilePictureUrl; }
    public void setUserProfilePictureUrl(String userProfilePictureUrl) { this.userProfilePictureUrl = userProfilePictureUrl; }
    public Set<Contact> getUserContactList() { return userContactList; }
    public void setUserContactList(Set<Contact> userContactList) { this.userContactList = userContactList; }
    public Set<Group> getUserGroupList() { return userGroupList; }
    public void setUserGroupList(Set<Group> userGroupList) { this.userGroupList = userGroupList; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
    public OnlineStatus getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(OnlineStatus onlineStatus) { this.onlineStatus = onlineStatus; }

    public void updateDescription(String description) {
        this.userDescription = description;
    }

    public void updateStatus(String status) {
        this.userStatus = status;
    }

    public void updateProfilePicture(String pictureUrl) {
        this.userProfilePictureUrl = pictureUrl;
    }

    public void updateOnlineStatus(OnlineStatus status) {
        this.onlineStatus = status;
        this.lastSeen = LocalDateTime.now();
    }
}
