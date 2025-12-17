package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.Notification;
import DAY6.WhatsApp.models.NotificationRecipient;
import DAY6.WhatsApp.models.User;
import DAY6.WhatsApp.enums.NotificationType;
import DAY6.WhatsApp.repositories.NotificationRepository;
import DAY6.WhatsApp.repositories.NotificationRepositoryImpl;
import DAY6.WhatsApp.repositories.NotificationRecipientRepository;
import DAY6.WhatsApp.repositories.NotificationRecipientRepositoryImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationService {
    private static NotificationService instance;
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository recipientRepository;
    private final UserService userService;
    
    private NotificationService(NotificationRepository notificationRepository,
                               NotificationRecipientRepository recipientRepository,
                               UserService userService) {
        this.notificationRepository = notificationRepository;
        this.recipientRepository = recipientRepository;
        this.userService = userService;
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService(
                NotificationRepositoryImpl.getInstance(),
                NotificationRecipientRepositoryImpl.getInstance(),
                UserService.getInstance()
            );
        }
        return instance;
    }
    
    public Notification createNotification(String senderId, NotificationType type, String description, String relatedEntityId, List<String> recipientIds) {
        if (senderId == null || type == null || recipientIds == null || recipientIds.isEmpty()) {
            return null;
        }
        
        User sender = userService.getUserById(senderId);
        
        Notification notification = new Notification();
        notification.setNotificationID(UUID.randomUUID().toString());
        notification.setNotificationType(type);
        notification.setNotificationDescription(description != null ? description : "");
        notification.setNotificationSender(sender);
        notification.setNotificationDateAndTime(LocalDateTime.now());
        notification.setRead(false);
        notification.setRelatedEntityId(relatedEntityId);
        
        Notification saved = notificationRepository.save(notification);
        
        for (String recipientId : recipientIds) {
            NotificationRecipient recipient = new NotificationRecipient();
            recipient.setNotificationID(notification.getNotificationID());
            recipient.setUserID(recipientId);
            recipient.setRead(false);
            recipient.setCreatedAt(LocalDateTime.now());
            recipientRepository.save(recipient);
        }
        
        return saved;
    }
    
    public Notification getNotificationById(String notificationId) {
        return notificationRepository.findById(notificationId);
    }
    
    public List<Notification> getNotificationsForUser(String userId) {
        List<NotificationRecipient> recipients = recipientRepository.findByUserId(userId);
        List<Notification> notifications = new ArrayList<>();
        for (NotificationRecipient recipient : recipients) {
            Notification notification = notificationRepository.findById(recipient.getNotificationID());
            if (notification != null) {
                notifications.add(notification);
            }
        }
        return notifications;
    }
    
    public List<Notification> getUnreadNotificationsForUser(String userId) {
        List<NotificationRecipient> recipients = recipientRepository.findByUserId(userId);
        List<Notification> unreadNotifications = new ArrayList<>();
        for (NotificationRecipient recipient : recipients) {
            if (!recipient.isRead()) {
                Notification notification = notificationRepository.findById(recipient.getNotificationID());
                if (notification != null) {
                    unreadNotifications.add(notification);
                }
            }
        }
        return unreadNotifications;
    }
    
    public boolean markAsReadForUser(String notificationId, String userId) {
        NotificationRecipient recipient = recipientRepository.findByNotificationAndUser(notificationId, userId);
        if (recipient == null) return false;
        
        recipient.markAsRead();
        recipientRepository.update(recipient);
        return true;
    }
    
    public boolean markAllAsReadForUser(String userId) {
        List<NotificationRecipient> recipients = recipientRepository.findByUserId(userId);
        for (NotificationRecipient recipient : recipients) {
            if (!recipient.isRead()) {
                recipient.markAsRead();
                recipientRepository.update(recipient);
            }
        }
        return true;
    }
    
    public List<NotificationRecipient> getRecipientsForNotification(String notificationId) {
        return recipientRepository.findByNotificationId(notificationId);
    }
    
    public int getUnreadCountForUser(String userId) {
        List<NotificationRecipient> recipients = recipientRepository.findByUserId(userId);
        int count = 0;
        for (NotificationRecipient recipient : recipients) {
            if (!recipient.isRead()) {
                count++;
            }
        }
        return count;
    }
    
    public boolean deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId);
        if (notification == null) return false;
        
        List<NotificationRecipient> recipients = recipientRepository.findByNotificationId(notificationId);
        for (NotificationRecipient recipient : recipients) {
            recipientRepository.delete(notificationId, recipient.getUserID());
        }
        
        notificationRepository.delete(notificationId);
        return true;
    }
    
    public boolean deleteNotificationForUser(String notificationId, String userId) {
        NotificationRecipient recipient = recipientRepository.findByNotificationAndUser(notificationId, userId);
        if (recipient == null) return false;
        
        recipientRepository.delete(notificationId, userId);
        return true;
    }
    
    public Notification createMessageNotification(String senderId, String recipientId, String messageContent) {
        List<String> recipients = new ArrayList<>();
        recipients.add(recipientId);
        return createNotification(senderId, NotificationType.NEW_MESSAGE, "New message: " + messageContent, null, recipients);
    }
    
    public Notification createCallNotification(String callerId, String receiverId, String callId) {
        List<String> recipients = new ArrayList<>();
        recipients.add(receiverId);
        return createNotification(callerId, NotificationType.CALL, "Incoming call", callId, recipients);
    }
    
    public Notification createGroupNotification(String senderId, List<String> groupMemberIds, String groupId, String description) {
        return createNotification(senderId, NotificationType.GROUP_UPDATE, description, groupId, groupMemberIds);
    }
}
