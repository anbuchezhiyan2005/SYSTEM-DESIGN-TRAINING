import React, { useState, useEffect } from 'react';
import './ChatApp.css';
import Sidebar from './Sidebar';
import ChatWindow from './ChatWindow';

function ChatApp({ user, apiBase, onLogout }) {
  const [contacts, setContacts] = useState([]);
  const [selectedContact, setSelectedContact] = useState(null);

  useEffect(() => {
    loadContacts();
  }, []);

  const loadContacts = async () => {
    try {
      const res = await fetch(`${apiBase}/contacts?userId=${user.userId}`);
      const data = await res.json();
      setContacts(data.contacts || []);
    } catch (err) {
      console.error('Failed to load contacts:', err);
    }
  };

  const handleAddContact = async (phone, firstName, lastName) => {
    try {
      await fetch(`${apiBase}/contacts?userId=${user.userId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ phone, firstName, lastName: lastName || '' })
      });
      loadContacts();
    } catch (err) {
      alert('Failed to add contact');
    }
  };

  return (
    <div className="chat-app">
      <Sidebar
        user={user}
        contacts={contacts}
        selectedContact={selectedContact}
        onSelectContact={setSelectedContact}
        onAddContact={handleAddContact}
        onLogout={onLogout}
      />
      <ChatWindow
        user={user}
        contact={selectedContact}
        apiBase={apiBase}
      />
    </div>
  );
}

export default ChatApp;
