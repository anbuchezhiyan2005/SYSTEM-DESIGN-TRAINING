package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.User;

public interface UserRepository {
    User save(User user);
    User findById(String userId);
    User findByMobileNumber(String mobileNumber);
    boolean existsByMobileNumber(String mobileNumber);
    User update(User user);
}
