import React, { useState, useEffect, useRef } from 'react';
import './GroupChatWindow.css';

function GroupChatWindow({ user, group, apiBase }) {
  const [messages, setMessages] = useState([]);
  const [inputText, setInputText] = useState('');
  const [members, setMembers] = useState([]);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    if (group) {
      loadMembers();
      loadMessages();
      const interval = setInterval(loadMessages, 2000);
      return () => clearInterval(interval);
    } else {
      setMessages([]);
      setMembers([]);
    }
  }, [group]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const loadMembers = async () => {
    try {
      const res = await fetch(`${apiBase}/groups/members?groupId=${group.groupId}`);
      const data = await res.json();
      setMembers(data.members || []);
    } catch (err) {
      console.error('Error loading members:', err);
    }
  };

  const loadMessages = async () => {
    if (!group) return;
    
    try {
      const res = await fetch(`${apiBase}/groups/messages?groupId=${group.groupId}`);
      const data = await res.json();
      const sortedMessages = (data.messages || []).sort((a, b) => 
        new Date(a.messageTime) - new Date(b.messageTime)
      );
      setMessages(sortedMessages);
    } catch (err) {
      console.error('Error loading messages:', err);
    }
  };

  const sendMessage = async () => {
    if (!inputText.trim() || !group) return;

    const content = inputText.trim();
    setInputText('');

    try {
      await fetch(`${apiBase}/groups/messages`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          groupId: group.groupId,
          senderId: user.userId,
          content
        })
      });
      loadMessages();
    } catch (err) {
      console.error('Error sending message:', err);
    }
  };

  if (!group) {
    return (
      <div className="group-chat-window">
        <div className="empty-chat">
          <h2>WhatsApp Groups</h2>
          <p>Select a group to start messaging</p>
        </div>
        <div className="input-area disabled">
          <input
            type="text"
            placeholder="Select a group to start messaging"
            disabled
          />
          <button disabled>Send</button>
        </div>
      </div>
    );
  }

  return (
    <div className="group-chat-window">
      <div className="chat-header">
        <div className="group-avatar">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
          </svg>
        </div>
        <div className="group-info">
          <div className="group-name">{group.groupName}</div>
          <div className="group-members-count">{members.length} members</div>
        </div>
      </div>

      <div className="messages-container">
        {messages.length === 0 ? (
          <div className="no-messages">
            <p>No messages yet. Start the conversation!</p>
          </div>
        ) : (
          messages.map((msg, idx) => (
            <div
              key={idx}
              className={`message ${msg.senderId === user.userId ? 'sent' : 'received'}`}
            >
              <div className="message-bubble">
                {msg.senderId !== user.userId && (
                  <div className="message-sender">{msg.senderName}</div>
                )}
                <div className="message-content">{msg.content}</div>
                <div className="message-time">
                  {new Date(msg.messageTime).toLocaleTimeString('en-US', { 
                    hour: 'numeric', 
                    minute: '2-digit',
                    hour12: true
                  })}
                </div>
              </div>
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      <div className="input-area">
        <input
          type="text"
          placeholder="Type a message"
          value={inputText}
          onChange={(e) => setInputText(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}

export default GroupChatWindow;
