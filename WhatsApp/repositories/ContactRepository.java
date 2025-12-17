package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Contact;
import java.util.List;

public interface ContactRepository {
    Contact save(Contact contact);
    Contact findById(String contactId);
    List<Contact> findByUserId(String userId);
    Contact update(Contact contact);
    void delete(String contactId);
    boolean existsByUserIdAndPhone(String userId, String mobileNumber);
}
