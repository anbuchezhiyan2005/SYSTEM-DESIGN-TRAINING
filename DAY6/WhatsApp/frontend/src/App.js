import React, { useState, useEffect } from 'react';
import './App.css';
import LoginScreen from './components/LoginScreen';
import ChatApp from './components/ChatApp';

const API_BASE = 'http://localhost:8080/api';

function App() {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem('whatsappUser');
    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
  }, []);

  const handleLogin = (user) => {
    setCurrentUser(user);
    localStorage.setItem('whatsappUser', JSON.stringify(user));
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('whatsappUser');
  };

  return (
    <div className="App">
      {!currentUser ? (
        <LoginScreen onLogin={handleLogin} apiBase={API_BASE} />
      ) : (
        <ChatApp user={currentUser} apiBase={API_BASE} onLogout={handleLogout} />
      )}
    </div>
  );
}

export default App;
