import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import HRJobsPage from './pages/hr/HRJobsPage';
import CandidateJobsPage from './pages/candidate/CandidateJobsPage';

// A helper component that redirects unauthenticated users to /login
// This is your ProtectedRoute — checks localStorage for a token
function ProtectedRoute({ children, allowedRole }: { children: JSX.Element, allowedRole: string }) {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (!token) return <Navigate to="/login" replace />;
  if (role !== allowedRole) return <Navigate to="/login" replace />;

  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        {/* HR routes — only accessible if role === HR */}
        <Route path="/hr/jobs" element={
          <ProtectedRoute allowedRole="HR">
            <HRJobsPage />
          </ProtectedRoute>
        } />

        {/* Candidate routes */}
        <Route path="/candidate/jobs" element={
          <ProtectedRoute allowedRole="CANDIDATE">
            <CandidateJobsPage />
          </ProtectedRoute>
        } />

        {/* Default redirect */}
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}