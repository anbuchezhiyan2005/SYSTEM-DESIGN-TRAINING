package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.Contact;
import DAY6.WhatsApp.repositories.ContactRepository;
import DAY6.WhatsApp.repositories.ContactRepositoryImpl;

import java.util.List;
import java.util.UUID;

public class ContactService {
	private static ContactService instance;
	private final ContactRepository contactRepository;

	private ContactService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	public static ContactService getInstance() {
		if (instance == null) {
			instance = new ContactService(ContactRepositoryImpl.getInstance());
		}
		return instance;
	}

	public Contact addContact(String userId, String mobileNumber, String firstName, String lastName, String region, String label) {
		if (userId == null || mobileNumber == null) return null;
		if (contactRepository.existsByUserIdAndPhone(userId, mobileNumber)) return null;
		Contact c = new Contact();
		c.setContactID(UUID.randomUUID().toString());
		c.setUserID(userId);
		c.setContactMobileNumber(mobileNumber);
		c.setContactFirstName(firstName);
		c.setContactLastName(lastName);
		c.setContactRegion(region);
		c.setContactLabel(label);
		c.setBlocked(false);
		return contactRepository.save(c);
	}

	public Contact getContactById(String contactId) { return contactRepository.findById(contactId); }

	public List<Contact> getContactsForUser(String userId) { return contactRepository.findByUserId(userId); }

	public Contact updateContact(Contact updated) {
		if (updated == null || updated.getContactID() == null || updated.getUserID() == null) return null;
		Contact existing = contactRepository.findById(updated.getContactID());
		if (existing == null) return null;
		if (!updated.getUserID().equals(existing.getUserID())) return null;
		return contactRepository.update(updated);
	}

	public boolean removeContact(String contactId) {
		Contact existing = contactRepository.findById(contactId);
		if (existing == null) return false;
		contactRepository.delete(contactId);
		return true;
	}

	public boolean blockContact(String contactId) {
		Contact c = contactRepository.findById(contactId);
		if (c == null) return false;
		if (c.isBlocked()) return true;
		c.setBlocked(true);
		return contactRepository.update(c) != null;
	}

	public boolean unblockContact(String contactId) {
		Contact c = contactRepository.findById(contactId);
		if (c == null) return false;
		if (!c.isBlocked()) return true;
		c.setBlocked(false);
		return contactRepository.update(c) != null;
	}

	public boolean linkToUser(String contactId, String linkedUserId) {
		Contact c = contactRepository.findById(contactId);
		if (c == null) return false;
		c.setLinkedUserId(linkedUserId);
		return contactRepository.update(c) != null;
	}

	public boolean unlinkFromUser(String contactId) {
		Contact c = contactRepository.findById(contactId);
		if (c == null) return false;
		c.setLinkedUserId(null);
		return contactRepository.update(c) != null;
	}

	public Contact findByPhone(String userId, String mobileNumber) {
		List<Contact> contacts = contactRepository.findByUserId(userId);
		for (Contact c : contacts) {
			if (mobileNumber != null && mobileNumber.equals(c.getContactMobileNumber())) return c;
		}
		return null;
	}
}
