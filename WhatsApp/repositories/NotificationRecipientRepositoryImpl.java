package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.NotificationRecipient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRecipientRepositoryImpl implements NotificationRecipientRepository {
    private Map<String, NotificationRecipient> recipientStore;
    private Map<String, List<String>> userNotificationsIndex;
    private Map<String, List<String>> notificationRecipientsIndex;
    private static NotificationRecipientRepositoryImpl instance;
    
    private NotificationRecipientRepositoryImpl() {
        this.recipientStore = new HashMap<>();
        this.userNotificationsIndex = new HashMap<>();
        this.notificationRecipientsIndex = new HashMap<>();
    }
    
    public static NotificationRecipientRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new NotificationRecipientRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public NotificationRecipient save(NotificationRecipient recipient) {
        if (recipient == null || recipient.getNotificationID() == null || recipient.getUserID() == null) {
            return null;
        }
        
        String key = createKey(recipient.getNotificationID(), recipient.getUserID());
        recipientStore.put(key, recipient);
        
        userNotificationsIndex.putIfAbsent(recipient.getUserID(), new ArrayList<>());
        if (!userNotificationsIndex.get(recipient.getUserID()).contains(recipient.getNotificationID())) {
            userNotificationsIndex.get(recipient.getUserID()).add(recipient.getNotificationID());
        }
        
        notificationRecipientsIndex.putIfAbsent(recipient.getNotificationID(), new ArrayList<>());
        if (!notificationRecipientsIndex.get(recipient.getNotificationID()).contains(recipient.getUserID())) {
            notificationRecipientsIndex.get(recipient.getNotificationID()).add(recipient.getUserID());
        }
        
        return recipient;
    }
    
    @Override
    public NotificationRecipient findByNotificationAndUser(String notificationId, String userId) {
        String key = createKey(notificationId, userId);
        return recipientStore.get(key);
    }
    
    @Override
    public List<NotificationRecipient> findByNotificationId(String notificationId) {
        List<NotificationRecipient> recipients = new ArrayList<>();
        List<String> userIds = notificationRecipientsIndex.getOrDefault(notificationId, new ArrayList<>());
        for (String userId : userIds) {
            String key = createKey(notificationId, userId);
            NotificationRecipient recipient = recipientStore.get(key);
            if (recipient != null) {
                recipients.add(recipient);
            }
        }
        return recipients;
    }
    
    @Override
    public List<NotificationRecipient> findByUserId(String userId) {
        List<NotificationRecipient> recipients = new ArrayList<>();
        List<String> notificationIds = userNotificationsIndex.getOrDefault(userId, new ArrayList<>());
        for (String notificationId : notificationIds) {
            String key = createKey(notificationId, userId);
            NotificationRecipient recipient = recipientStore.get(key);
            if (recipient != null) {
                recipients.add(recipient);
            }
        }
        return recipients;
    }
    
    @Override
    public NotificationRecipient update(NotificationRecipient recipient) {
        if (recipient == null || recipient.getNotificationID() == null || recipient.getUserID() == null) {
            return null;
        }
        String key = createKey(recipient.getNotificationID(), recipient.getUserID());
        if (!recipientStore.containsKey(key)) {
            return null;
        }
        recipientStore.put(key, recipient);
        return recipient;
    }
    
    @Override
    public void delete(String notificationId, String userId) {
        String key = createKey(notificationId, userId);
        recipientStore.remove(key);
        
        List<String> userNotifications = userNotificationsIndex.get(userId);
        if (userNotifications != null) {
            userNotifications.remove(notificationId);
        }
        
        List<String> notificationRecipients = notificationRecipientsIndex.get(notificationId);
        if (notificationRecipients != null) {
            notificationRecipients.remove(userId);
        }
    }
    
    private String createKey(String notificationId, String userId) {
        return notificationId + ":" + userId;
    }
}
