package DAY6.WhatsApp.models;

public class Contact {
    private String userID;
    private String contactID;
    private String contactMobileNumber;
    private String contactFirstName;
    private String contactLastName;
    private String contactRegion;
    private String contactLabel;
    private String linkedUserId;
    private boolean isBlocked;

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactMobileNumber() {
        return contactMobileNumber;
    }

    public void setContactMobileNumber(String contactMobileNumber) {
        this.contactMobileNumber = contactMobileNumber;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactRegion() {
        return contactRegion;
    }

    public void setContactRegion(String contactRegion) {
        this.contactRegion = contactRegion;
    }

    public String getContactLabel() {
        return contactLabel;
    }

    public void setContactLabel(String contactLabel) {
        this.contactLabel = contactLabel;
    }

    public String getLinkedUserId() {
        return linkedUserId;
    }

    public void setLinkedUserId(String linkedUserId) {
        this.linkedUserId = linkedUserId;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
    
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

}