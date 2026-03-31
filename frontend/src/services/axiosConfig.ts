import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

// This "interceptor" runs before every request you make
// It reads the token from storage and adds it to the header automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// This interceptor handles expired tokens
// If the server returns 401, clear storage and redirect to login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;