import api from './axiosConfig';

// TypeScript interface — think of this as a contract that describes
// the shape of data coming back from your API
export interface JobResponse {
  id: number;
  title: string;
  description: string;
  skillsRequired: string;
  yearsOfExperience: number;
  postedDate: string;
  postedByName: string;
}

export interface JobRequest {
  title: string;
  description: string;
  skillsRequired: string;
  yearsOfExperience: number;
}

// All job-related API calls live here — if the URL ever changes,
// you change it in one place, not scattered across 10 components
export const jobService = {
  getAll: () => api.get<JobResponse[]>('/api/jobs'),
  getById: (id: number) => api.get<JobResponse>(`/api/jobs/${id}`),
  create: (data: JobRequest) => api.post<JobResponse>('/api/jobs', data),
  update: (id: number, data: JobRequest) => api.put<JobResponse>(`/api/jobs/${id}`, data),
  delete: (id: number) => api.delete(`/api/jobs/${id}`),
};

export const applicationService = {
  apply: (jobId: number, resume: File) => {
    // FormData is how you send files over HTTP from a browser
    const formData = new FormData();
    formData.append('jobId', String(jobId));
    formData.append('resume', resume);   // must match @RequestParam name in controller
    return api.post('/api/applications/apply', formData);
  },
  getMyApplications: () => api.get('/api/applications/mine'),
  getByJob: (jobId: number) => api.get(`/api/applications/job/${jobId}`),
};