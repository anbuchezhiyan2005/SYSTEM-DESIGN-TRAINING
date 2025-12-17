package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Notification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepositoryImpl implements NotificationRepository {
    private Map<String, Notification> notificationStore;
    private static NotificationRepositoryImpl instance;
    
    private NotificationRepositoryImpl() {
        this.notificationStore = new HashMap<>();
    }
    
    public static NotificationRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new NotificationRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public Notification save(Notification notification) {
        if (notification == null || notification.getNotificationID() == null) {
            return null;
        }
        notificationStore.put(notification.getNotificationID(), notification);
        return notification;
    }
    
    @Override
    public Notification findById(String notificationId) {
        return notificationStore.get(notificationId);
    }
    
    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notificationStore.values());
    }
    
    @Override
    public Notification update(Notification notification) {
        if (notification == null || notification.getNotificationID() == null) {
            return null;
        }
        if (!notificationStore.containsKey(notification.getNotificationID())) {
            return null;
        }
        notificationStore.put(notification.getNotificationID(), notification);
        return notification;
    }
    
    @Override
    public void delete(String notificationId) {
        notificationStore.remove(notificationId);
    }
}
