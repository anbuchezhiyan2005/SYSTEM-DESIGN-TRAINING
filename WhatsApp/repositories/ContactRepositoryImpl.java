package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactRepositoryImpl implements ContactRepository {
    private Map<String, Contact> contactStore;
    private Map<String, List<String>> userContactsIndex;  
    
    private static ContactRepositoryImpl instance;
    
    private ContactRepositoryImpl() {
        this.contactStore = new HashMap<>();
        this.userContactsIndex = new HashMap<>();
    }
    
    public static ContactRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ContactRepositoryImpl();
        }
        return instance;
    }
    
    public Contact save(Contact contact) {
        if (contact == null || contact.getContactID() == null) {
            return null;
        }
        contactStore.put(contact.getContactID(), contact);
        
        String userId = contact.getUserID();
        if (userId != null) {
            userContactsIndex.putIfAbsent(userId, new ArrayList<>());
            userContactsIndex.get(userId).add(contact.getContactID());
        }
        
        return contact;
    }
    
    public Contact findById(String contactId) {
        return contactStore.get(contactId);
    }
    
    public List<Contact> findByUserId(String userId) {
        List<Contact> userContacts = new ArrayList<>();
        List<String> contactIds = userContactsIndex.getOrDefault(userId, new ArrayList<>());
        for (String contactId : contactIds) {
            Contact contact = contactStore.get(contactId);
            if (contact != null) {
                userContacts.add(contact);
            }
        }
        return userContacts;
    }
    
    public Contact update(Contact contact) {
        if (contact == null || contact.getContactID() == null) {
            return null;
        }
        if (!contactStore.containsKey(contact.getContactID())) {
            return null;
        }
        contactStore.put(contact.getContactID(), contact);
        return contact;
    }
    
    public void delete(String contactId) {
        Contact contact = contactStore.get(contactId);
        if (contact != null) {
            contactStore.remove(contactId);
            String userId = contact.getUserID();
            List<String> userContacts = userContactsIndex.get(userId);
            if (userContacts != null) {
                userContacts.remove(contactId);
            }
        }
    }
    
    public boolean existsByUserIdAndPhone(String userId, String mobileNumber) {
        List<Contact> userContacts = findByUserId(userId);
        for (Contact contact : userContacts) {
            if (contact.getContactMobileNumber().equals(mobileNumber)) {
                return true;
            }
        }
        return false;
    }
}
