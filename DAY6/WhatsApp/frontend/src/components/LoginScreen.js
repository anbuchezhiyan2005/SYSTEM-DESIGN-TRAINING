import React, { useState } from 'react';
import './LoginScreen.css';

function LoginScreen({ onLogin, apiBase }) {
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleRegister = async () => {
    if (!name || !phone) {
      setError('Please enter both name and phone number');
      return;
    }
    setLoading(true);
    setError('');
    
    try {
      const res = await fetch(`${apiBase}/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, phone })
      });
      const data = await res.json();
      
      if (data.success) {
        onLogin(data);
      } else {
        setError(data.error || 'Registration failed');
      }
    } catch (err) {
      setError('Connection error. Make sure the server is running.');
    }
    setLoading(false);
  };

  const handleLogin = async () => {
    if (!phone) {
      setError('Please enter phone number');
      return;
    }
    setLoading(true);
    setError('');
    
    try {
      const res = await fetch(`${apiBase}/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ phone })
      });
      const data = await res.json();
      
      if (data.success) {
        onLogin(data);
      } else {
        setError('User not found. Please register first.');
      }
    } catch (err) {
      setError('Connection error. Make sure the server is running.');
    }
    setLoading(false);
  };

  return (
    <div className="login-screen">
      <div className="login-box">
        <h1>WhatsApp</h1>
        <input
          type="text"
          placeholder="Your Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          disabled={loading}
        />
        <input
          type="tel"
          placeholder="Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          disabled={loading}
          onKeyPress={(e) => e.key === 'Enter' && handleLogin()}
        />
        {error && <div className="error">{error}</div>}
        <button onClick={handleRegister} disabled={loading}>
          {loading ? 'Please wait...' : 'Register'}
        </button>
        <button onClick={handleLogin} disabled={loading} className="secondary">
          {loading ? 'Please wait...' : 'Login'}
        </button>
      </div>
    </div>
  );
}

export default LoginScreen;
