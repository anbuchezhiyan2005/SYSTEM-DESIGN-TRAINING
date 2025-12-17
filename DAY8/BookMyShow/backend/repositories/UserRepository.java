package DAY8.BookMyShow.backend.repositories;

import DAY8.BookMyShow.backend.models.User;

public interface UserRepository {
    User save(User user);
    User findById(String userId);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean delete(String userId);
}
