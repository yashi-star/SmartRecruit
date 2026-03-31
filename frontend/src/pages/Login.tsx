import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/axiosConfig';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', { email, password });
      const { token, role, name, userId } = response.data;

      // Store in localStorage — your axiosConfig will pick this up automatically
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      localStorage.setItem('name', name);
      localStorage.setItem('userId', String(userId));

      // Redirect based on role — each role gets their own panel
      if (role === 'HR') navigate('/hr/dashboard');
      else if (role === 'INTERVIEWER') navigate('/interviewer/dashboard');
      else navigate('/candidate/jobs');

    } catch (err) {
      setError('Invalid email or password');
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '80px auto', padding: '2rem' }}>
      <h2>Sign in</h2>
      <form onSubmit={handleLogin}>
        <input type="email" placeholder="Email" value={email}
               onChange={e => setEmail(e.target.value)} style={{ display:'block', width:'100%', marginBottom:12, padding:10 }}/>
        <input type="password" placeholder="Password" value={password}
               onChange={e => setPassword(e.target.value)} style={{ display:'block', width:'100%', marginBottom:12, padding:10 }}/>
        {error && <p style={{ color: 'red', fontSize: 13 }}>{error}</p>}
        <button type="submit" style={{ width:'100%', padding:10 }}>Sign in</button>
      </form>
    </div>
  );
}