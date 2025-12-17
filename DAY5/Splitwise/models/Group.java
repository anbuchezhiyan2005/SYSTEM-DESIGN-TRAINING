// Group class: groupName, groupDescription, groupAdmin, groupTotalExpense, groupMembers

package Splitwise.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private String groupDescription;
    private User groupAdmin;
    private double groupTotalExpense;
    private List<User> groupMembers;

    public Group(String groupName, String groupDescription, User groupAdmin) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupAdmin = groupAdmin;
        this.groupMembers = new ArrayList<>();
        this.groupTotalExpense = 0.0;
        this.groupMembers.add(groupAdmin);
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public User getGroupAdmin() {
        return groupAdmin;
    }

    public double getGroupTotalExpense() {
        return groupTotalExpense;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }

    public void addMember(User user) {
        if(!groupMembers.contains(user)) {
            groupMembers.add(user);
            System.out.println(user.getUserName() + " added to group " + this.groupName);
        } else {
            System.out.println(user.getUserName() + " is already a member of group " + this.groupName);
        }
    }

    public void removeMember(User user) {
        if(groupMembers.contains(user)) {
            groupMembers.remove(user);
            System.out.println(user.getUserName() + " removed from group " + this.groupName);
        } else {
            System.out.println(user.getUserName() + " is not a member of group " + this.groupName);
        }
    }
}
