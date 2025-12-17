package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupRepositoryImpl implements GroupRepository {
    private Map<String, Group> groupStore;
    private static GroupRepositoryImpl instance;
    
    private GroupRepositoryImpl() {
        this.groupStore = new HashMap<>();
    }
    
    public static GroupRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public Group save(Group group) {
        if (group == null || group.getGroupID() == null) {
            return null;
        }
        groupStore.put(group.getGroupID(), group);
        return group;
    }
    
    @Override
    public Group findById(String groupId) {
        return groupStore.get(groupId);
    }
    
    @Override
    public List<Group> findAll() {
        return new ArrayList<>(groupStore.values());
    }
    
    @Override
    public Group update(Group group) {
        if (group == null || group.getGroupID() == null) {
            return null;
        }
        if (!groupStore.containsKey(group.getGroupID())) {
            return null;
        }
        groupStore.put(group.getGroupID(), group);
        return group;
    }
    
    @Override
    public void delete(String groupId) {
        groupStore.remove(groupId);
    }
}
