package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> userStore;           
    private Map<String, String> phoneIndex;        
    
    private static UserRepositoryImpl instance;
    
    private UserRepositoryImpl() {
        this.userStore = new HashMap<>();
        this.phoneIndex = new HashMap<>();
    }
    
    public static UserRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }
    
    public User save(User user) {
        if (user == null || user.getUserID() == null) {
            return null;
        }
        userStore.put(user.getUserID(), user);
        phoneIndex.put(user.getUserMobileNumber(), user.getUserID());
        return user;
    }
    
    public User findById(String userId) {
        return userStore.get(userId);
    }
    
    public User findByMobileNumber(String mobileNumber) {
        String userId = phoneIndex.get(mobileNumber);
        return userId != null ? userStore.get(userId) : null;
    }
    
    public boolean existsByMobileNumber(String mobileNumber) {
        return phoneIndex.containsKey(mobileNumber);
    }
    
    public User update(User user) {
        if (user == null || user.getUserID() == null) {
            return null;
        }
        User existing = userStore.get(user.getUserID());
        if (existing == null) {
            return null;
        }
        String oldMobile = existing.getUserMobileNumber();
        String newMobile = user.getUserMobileNumber();
        if (oldMobile != null && !oldMobile.equals(newMobile)) {
            phoneIndex.remove(oldMobile);
        }
        userStore.put(user.getUserID(), user);
        phoneIndex.put(newMobile, user.getUserID());
        return user;
    }
}
