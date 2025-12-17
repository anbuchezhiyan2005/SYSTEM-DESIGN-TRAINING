package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Group;
import java.util.List;

public interface GroupRepository {
    Group save(Group group);
    Group findById(String groupId);
    List<Group> findAll();
    Group update(Group group);
    void delete(String groupId);
}
