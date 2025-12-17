import React, { useState, useEffect, useRef } from 'react';
import './ChatWindow.css';

function ChatWindow({ user, contact, apiBase }) {
  const [messages, setMessages] = useState([]);
  const [inputText, setInputText] = useState('');
  const [receiverUserId, setReceiverUserId] = useState(null);
  const [chatId, setChatId] = useState(null);
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    if (contact) {
      loadContactUser();
    } else {
      setMessages([]);
      setReceiverUserId(null);
      setChatId(null);
    }
  }, [contact]);

  useEffect(() => {
    if (receiverUserId) {
      loadMessages();
      const interval = setInterval(loadMessages, 2000);
      return () => clearInterval(interval);
    }
  }, [receiverUserId]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const loadContactUser = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${apiBase}/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ phone: contact.phone })
      });
      const data = await res.json();
      if (data.success) {
        setReceiverUserId(data.userId);
      } else {
        setReceiverUserId(null);
      }
    } catch (err) {
      console.error('Error loading contact user:', err);
    }
    setLoading(false);
  };

  const loadMessages = async () => {
    if (!receiverUserId) return;

    try {
      const chatsRes = await fetch(`${apiBase}/chats?userId=${user.userId}`);
      const chatsData = await chatsRes.json();
      const existingChat = chatsData.chats?.find(c => c.otherUserId === receiverUserId);
      
      if (existingChat) {
        setChatId(existingChat.chatId);
        const msgRes = await fetch(`${apiBase}/messages?chatId=${existingChat.chatId}`);
        const msgData = await msgRes.json();
        const sortedMessages = (msgData.messages || []).sort((a, b) => 
          new Date(a.messageTime) - new Date(b.messageTime)
        );
        setMessages(sortedMessages);
      } else {
        setMessages([]);
      }
    } catch (err) {
      console.error('Error loading messages:', err);
    }
  };

  const sendMessage = async () => {
    if (!inputText.trim() || !receiverUserId) return;

    const content = inputText.trim();
    setInputText('');

    try {
      await fetch(`${apiBase}/messages`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          senderId: user.userId,
          receiverId: receiverUserId,
          content
        })
      });
      loadMessages();
    } catch (err) {
      console.error('Error sending message:', err);
    }
  };

  if (!contact) {
    return (
      <div className="chat-window">
        <div className="empty-chat">
          <h2>WhatsApp Web</h2>
          <p>Select a contact to start messaging</p>
        </div>
        <div className="input-area disabled">
          <input
            type="text"
            placeholder="Select a contact to start messaging"
            disabled
          />
          <button disabled>Send</button>
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="chat-window">
        <div className="empty-chat">
          <p>Loading...</p>
        </div>
      </div>
    );
  }

  if (!receiverUserId) {
    return (
      <div className="chat-window">
        <div className="chat-header">
          <div className="contact-avatar">{contact.name[0].toUpperCase()}</div>
          <div className="contact-info">
            <div className="contact-name">{contact.name}</div>
          </div>
        </div>
        <div className="empty-chat">
          <p>This contact hasn't registered yet</p>
          <p>They need to sign up to start chatting</p>
        </div>
        <div className="input-area disabled">
          <input
            type="text"
            placeholder="Contact not registered"
            disabled
          />
          <button disabled>Send</button>
        </div>
      </div>
    );
  }

  return (
    <div className="chat-window">
      <div className="chat-header">
        <div className="contact-avatar">{contact.name[0].toUpperCase()}</div>
        <div className="contact-info">
          <div className="contact-name">{contact.name}</div>
          <div className="contact-status">{contact.phone}</div>
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

export default ChatWindow;
