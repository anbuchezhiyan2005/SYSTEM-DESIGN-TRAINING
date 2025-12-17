package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.User;
import DAY6.WhatsApp.repositories.UserRepository;
import DAY6.WhatsApp.repositories.UserRepositoryImpl;
import DAY6.WhatsApp.enums.OnlineStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {
    private static UserService instance;
    private UserRepository userRepository;
    
    private UserService() {
        this.userRepository = UserRepositoryImpl.getInstance();
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User registerUser(String name, String mobileNumber) {
        if (name == null || name.trim().isEmpty() || mobileNumber == null || mobileNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Name and mobile number cannot be empty");
        }

        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new IllegalStateException("User with this mobile number already exists");
        }

        User user = new User();
        user.setUserID(UUID.randomUUID().toString());
        user.setUserName(name);
        user.setUserMobileNumber(mobileNumber);
        user.setUserDescription("");
        user.setUserStatus("");
        user.setUserProfilePictureUrl("");
        user.setOnlineStatus(OnlineStatus.OFFLINE);
        user.setLastSeen(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public User getUserByMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Mobile number cannot be empty");
        }
        User user = userRepository.findByMobileNumber(mobileNumber);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public void updateDescription(String userId, String description) {
        User user = getUserById(userId);
        user.updateDescription(description);
        userRepository.update(user);
    }

    public void updateStatus(String userId, String status) {
        User user = getUserById(userId);
        user.updateStatus(status);
        userRepository.update(user);
    }

    public void updateProfilePicture(String userId, String pictureUrl) {
        User user = getUserById(userId);
        user.updateProfilePicture(pictureUrl);
        userRepository.update(user);
    }

    public void updateOnlineStatus(String userId, OnlineStatus status) {
        User user = getUserById(userId);
        user.updateOnlineStatus(status);
        userRepository.update(user);
    }
}
