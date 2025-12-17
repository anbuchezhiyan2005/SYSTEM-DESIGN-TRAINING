import React, { useState } from 'react';
import './Sidebar.css';

function Sidebar({ user, contacts, selectedContact, onSelectContact, onAddContact, onLogout }) {
  const [showAddModal, setShowAddModal] = useState(false);
  const [newPhone, setNewPhone] = useState('');
  const [newFirstName, setNewFirstName] = useState('');
  const [newLastName, setNewLastName] = useState('');

  const handleAdd = () => {
    if (newPhone && newFirstName) {
      onAddContact(newPhone, newFirstName, newLastName);
      setNewPhone('');
      setNewFirstName('');
      setNewLastName('');
      setShowAddModal(false);
    }
  };

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <div className="user-info">
          <div className="user-avatar">{user.name[0].toUpperCase()}</div>
          <span>{user.name}</span>
        </div>
        <div className="header-actions">
          <button onClick={() => setShowAddModal(true)} title="Add Contact">+</button>
          <button onClick={onLogout} title="Logout">⎋</button>
        </div>
      </div>
      
      <div className="contacts-list">
        {contacts.length === 0 ? (
          <div className="empty-state">
            <p>No contacts yet</p>
            <p>Click + to add someone</p>
          </div>
        ) : (
          contacts.map((contact) => (
            <div
              key={contact.contactID}
              className={`contact-item ${selectedContact?.contactID === contact.contactID ? 'active' : ''}`}
              onClick={() => onSelectContact(contact)}
            >
              <div className="contact-avatar">{contact.name[0].toUpperCase()}</div>
              <div className="contact-info">
                <div className="contact-name">{contact.name}</div>
                <div className="contact-phone">{contact.phone}</div>
              </div>
            </div>
          ))
        )}
      </div>

      {showAddModal && (
        <div className="modal-overlay" onClick={() => setShowAddModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Add New Contact</h3>
            <input
              type="tel"
              placeholder="Phone Number"
              value={newPhone}
              onChange={(e) => setNewPhone(e.target.value)}
            />
            <input
              type="text"
              placeholder="First Name"
              value={newFirstName}
              onChange={(e) => setNewFirstName(e.target.value)}
            />
            <input
              type="text"
              placeholder="Last Name (optional)"
              value={newLastName}
              onChange={(e) => setNewLastName(e.target.value)}
            />
            <div className="modal-actions">
              <button onClick={handleAdd}>Add</button>
              <button onClick={() => setShowAddModal(false)} className="cancel">Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Sidebar;
