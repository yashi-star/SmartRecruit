import { useState, useEffect } from 'react';
import { jobService, JobResponse, JobRequest } from '../../services/jobService';

export default function HRJobsPage() {
  const [jobs, setJobs] = useState<JobResponse[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Form state — one object for all form fields, cleaner than multiple useState calls
  const [form, setForm] = useState<JobRequest>({
    title: '', description: '', skillsRequired: '', yearsOfExperience: 0
  });

  // Runs once when the component mounts — fetch all jobs
  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const response = await jobService.getAll();
      setJobs(response.data);
    } catch (err) {
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await jobService.create(form);
      // After creating, re-fetch the list so the UI updates immediately
      await fetchJobs();
      setShowForm(false);
      setForm({ title: '', description: '', skillsRequired: '', yearsOfExperience: 0 });
    } catch (err) {
      setError('Failed to create job');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Delete this job posting?')) return;
    try {
      await jobService.delete(id);
      // Remove from local state without re-fetching — more efficient
      setJobs(prev => prev.filter(j => j.id !== id));
    } catch (err) {
      setError('Failed to delete job');
    }
  };

  if (loading) return <div style={{ padding: '2rem' }}>Loading jobs...</div>;

  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: '2rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h1 style={{ fontSize: 22, fontWeight: 500 }}>Job Postings</h1>
        <button onClick={() => setShowForm(!showForm)}
          style={{ padding: '8px 20px', cursor: 'pointer' }}>
          {showForm ? 'Cancel' : '+ New Job'}
        </button>
      </div>

      {error && <p style={{ color: 'red', marginBottom: '1rem' }}>{error}</p>}

      {/* Create Job Form — only visible when HR clicks New Job */}
      {showForm && (
        <form onSubmit={handleCreate} style={{
          padding: '1.5rem', border: '0.5px solid #ddd',
          borderRadius: 8, marginBottom: '2rem', background: '#fafafa'
        }}>
          <h2 style={{ fontSize: 16, fontWeight: 500, marginBottom: '1rem' }}>Create new job posting</h2>

          <input placeholder="Job title" required value={form.title}
            onChange={e => setForm({ ...form, title: e.target.value })}
            style={{ display: 'block', width: '100%', marginBottom: 12, padding: 10, borderRadius: 6, border: '0.5px solid #ccc' }} />

          <textarea placeholder="Full job description" required value={form.description}
            onChange={e => setForm({ ...form, description: e.target.value })}
            rows={4} style={{ display: 'block', width: '100%', marginBottom: 12, padding: 10, borderRadius: 6, border: '0.5px solid #ccc' }} />

          <input placeholder="Required skills (e.g. Java, React, SQL)" required value={form.skillsRequired}
            onChange={e => setForm({ ...form, skillsRequired: e.target.value })}
            style={{ display: 'block', width: '100%', marginBottom: 12, padding: 10, borderRadius: 6, border: '0.5px solid #ccc' }} />

          <input type="number" placeholder="Years of experience required" required
            value={form.yearsOfExperience}
            onChange={e => setForm({ ...form, yearsOfExperience: Number(e.target.value) })}
            style={{ display: 'block', width: '100%', marginBottom: 16, padding: 10, borderRadius: 6, border: '0.5px solid #ccc' }} />

          <button type="submit" style={{ padding: '10px 28px', cursor: 'pointer' }}>
            Post Job
          </button>
        </form>
      )}

      {/* Jobs List */}
      {jobs.length === 0
        ? <p style={{ color: '#888' }}>No job postings yet. Create your first one above.</p>
        : jobs.map(job => (
          <div key={job.id} style={{
            padding: '1.25rem 1.5rem', border: '0.5px solid #e0e0e0',
            borderRadius: 8, marginBottom: 12, background: '#fff'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <h3 style={{ fontWeight: 500, marginBottom: 4 }}>{job.title}</h3>
                <p style={{ fontSize: 13, color: '#666', marginBottom: 6 }}>
                  Posted on {new Date(job.postedDate).toLocaleDateString()} by {job.postedByName}
                </p>
                <p style={{ fontSize: 13, color: '#444', marginBottom: 4 }}>
                  <strong>Skills:</strong> {job.skillsRequired}
                </p>
                <p style={{ fontSize: 13, color: '#444' }}>
                  <strong>Experience:</strong> {job.yearsOfExperience}+ years
                </p>
              </div>
              <button onClick={() => handleDelete(job.id)}
                style={{ padding: '6px 16px', cursor: 'pointer', color: '#c0392b', border: '0.5px solid #c0392b', borderRadius: 6, background: 'transparent' }}>
                Delete
              </button>
            </div>
          </div>
        ))
      }
    </div>
  );
}