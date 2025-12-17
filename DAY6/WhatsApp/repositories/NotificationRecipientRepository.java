package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.NotificationRecipient;
import java.util.List;

public interface NotificationRecipientRepository {
    NotificationRecipient save(NotificationRecipient recipient);
    NotificationRecipient findByNotificationAndUser(String notificationId, String userId);
    List<NotificationRecipient> findByNotificationId(String notificationId);
    List<NotificationRecipient> findByUserId(String userId);
    NotificationRecipient update(NotificationRecipient recipient);
    void delete(String notificationId, String userId);
}
