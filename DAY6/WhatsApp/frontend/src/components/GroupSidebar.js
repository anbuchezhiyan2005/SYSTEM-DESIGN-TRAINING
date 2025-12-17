import React, { useState } from 'react';
import './GroupSidebar.css';

function GroupSidebar({ user, groups, contacts, selectedGroup, onSelectGroup, onCreateGroup }) {
  const [showModal, setShowModal] = useState(false);
  const [groupName, setGroupName] = useState('');
  const [selectedMembers, setSelectedMembers] = useState([]);

  const handleToggleMember = (contactId) => {
    setSelectedMembers(prev => 
      prev.includes(contactId) 
        ? prev.filter(id => id !== contactId)
        : [...prev, contactId]
    );
  };

  const handleCreate = async () => {
    if (!groupName.trim() || selectedMembers.length === 0) {
      alert('Please enter group name and select at least one member');
      return;
    }

    await onCreateGroup(groupName, selectedMembers);
    setGroupName('');
    setSelectedMembers([]);
    setShowModal(false);
  };

  return (
    <div className="group-sidebar">
      <div className="sidebar-header">
        <div className="user-info">
          <div className="user-avatar">{user.name[0].toUpperCase()}</div>
          <span>Groups</span>
        </div>
        <button className="new-group-btn" onClick={() => setShowModal(true)}>+ New Group</button>
      </div>

      <div className="groups-list">
        {groups.length === 0 ? (
          <div className="empty-state">
            <p>No groups yet</p>
            <p>Create a group to start</p>
          </div>
        ) : (
          groups.map((group) => (
            <div
              key={group.groupId}
              className={`group-item ${selectedGroup?.groupId === group.groupId ? 'active' : ''}`}
              onClick={() => onSelectGroup(group)}
            >
              <div className="group-avatar">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
                </svg>
              </div>
              <div className="group-info">
                <div className="group-name">{group.groupName}</div>
                <div className="group-members">{group.memberCount} members</div>
              </div>
            </div>
          ))
        )}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Create New Group</h3>
            <input
              type="text"
              placeholder="Group Name"
              value={groupName}
              onChange={(e) => setGroupName(e.target.value)}
            />
            <div className="members-section">
              <h4>Add Members</h4>
              <div className="members-list">
                {contacts.length === 0 ? (
                  <p>No contacts available</p>
                ) : (
                  contacts.map((contact) => (
                    <label key={contact.contactID} className="member-checkbox">
                      <input
                        type="checkbox"
                        checked={selectedMembers.includes(contact.userID)}
                        onChange={() => handleToggleMember(contact.userID)}
                      />
                      <span>{contact.name}</span>
                    </label>
                  ))
                )}
              </div>
            </div>
            <div className="modal-actions">
              <button onClick={handleCreate}>Create Group</button>
              <button onClick={() => setShowModal(false)} className="cancel">Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default GroupSidebar;
