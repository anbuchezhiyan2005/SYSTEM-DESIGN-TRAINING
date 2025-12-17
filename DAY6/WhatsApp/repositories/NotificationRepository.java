package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Notification;
import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    Notification findById(String notificationId);
    List<Notification> findAll();
    Notification update(Notification notification);
    void delete(String notificationId);
}
