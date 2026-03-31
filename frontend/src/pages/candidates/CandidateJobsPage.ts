
import { useState, useEffect } from 'react';
import { jobService, JobResponse } from '../../services/jobService';
import { applicationService } from '../../services/jobService';

export default function CandidateJobsPage() {
  const [jobs, setJobs] = useState<JobResponse[]>([]);
  const [selectedJob, setSelectedJob] = useState<JobResponse | null>(null);
  const [resumeFile, setResumeFile] = useState<File | null>(null);
  const [applying, setApplying] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    jobService.getAll().then(res => setJobs(res.data));
  }, []);

  const handleApply = async () => {
    if (!selectedJob || !resumeFile) {
      setError('Please select a resume file before applying');
      return;
    }
    setApplying(true);
    setError('');
    try {
      await applicationService.apply(selectedJob.id, resumeFile);
      setSuccessMsg(`Successfully applied for ${selectedJob.title}! We will review your resume shortly.`);
      setSelectedJob(null);
      setResumeFile(null);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Application failed');
    } finally {
      setApplying(false);
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: '2rem' }}>
      <h1 style={{ fontSize: 22, fontWeight: 500, marginBottom: '1.5rem' }}>Available Positions</h1>

      {successMsg && (
        <div style={{ padding: '1rem', background: '#eafaf1', border: '0.5px solid #2ecc71', borderRadius: 8, marginBottom: '1.5rem', color: '#1a7a4a' }}>
          {successMsg}
        </div>
      )}

      {/* Job Cards Grid */}
      <div style={{ display: 'grid', gap: 16 }}>
        {jobs.map(job => (
          <div key={job.id} style={{
            padding: '1.25rem 1.5rem', border: '0.5px solid #e0e0e0',
            borderRadius: 8, background: '#fff'
          }}>
            {/* Job Header */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <h2 style={{ fontSize: 16, fontWeight: 500, marginBottom: 4 }}>{job.title}</h2>
                <p style={{ fontSize: 12, color: '#888' }}>
                  Posted {new Date(job.postedDate).toLocaleDateString()}
                </p>
              </div>
              {/* Quick Apply button — opens inline apply panel */}
              <button onClick={() => setSelectedJob(selectedJob?.id === job.id ? null : job)}
                style={{
                  padding: '7px 18px', cursor: 'pointer', borderRadius: 6,
                  background: selectedJob?.id === job.id ? '#eee' : '#2c3e50',
                  color: selectedJob?.id === job.id ? '#333' : '#fff',
                  border: 'none', fontSize: 13
                }}>
                {selectedJob?.id === job.id ? 'Close' : 'Quick Apply'}
              </button>
            </div>

            {/* Job Details */}
            <div style={{ marginTop: 12, display: 'flex', gap: 24, flexWrap: 'wrap' }}>
              <span style={{ fontSize: 13, color: '#555' }}>
                📋 <strong>Skills:</strong> {job.skillsRequired}
              </span>
              <span style={{ fontSize: 13, color: '#555' }}>
                🎓 <strong>Experience:</strong> {job.yearsOfExperience}+ years
              </span>
            </div>

            <p style={{ marginTop: 10, fontSize: 13, color: '#666', lineHeight: 1.6 }}>
              {job.description}
            </p>

            {/* Inline Apply Panel — only shows for the selected job */}
            {selectedJob?.id === job.id && (
              <div style={{
                marginTop: 16, padding: '1rem', background: '#f8f9fa',
                borderRadius: 6, border: '0.5px solid #dee2e6'
              }}>
                <p style={{ fontSize: 13, fontWeight: 500, marginBottom: 10 }}>
                  Applying for: <em>{job.title}</em>
                </p>

                <label style={{ fontSize: 13, color: '#555', display: 'block', marginBottom: 8 }}>
                  Upload your resume (PDF only)
                </label>
                <input type="file" accept=".pdf,.doc,.docx"
                  onChange={e => setResumeFile(e.target.files?.[0] || null)}
                  style={{ display: 'block', marginBottom: 12, fontSize: 13 }} />

                {error && <p style={{ color: 'red', fontSize: 13, marginBottom: 8 }}>{error}</p>}

                <button onClick={handleApply} disabled={applying || !resumeFile}
                  style={{
                    padding: '9px 24px', cursor: applying ? 'not-allowed' : 'pointer',
                    background: '#27ae60', color: '#fff', border: 'none', borderRadius: 6,
                    opacity: applying || !resumeFile ? 0.6 : 1
                  }}>
                  {applying ? 'Submitting...' : 'Submit Application'}
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}