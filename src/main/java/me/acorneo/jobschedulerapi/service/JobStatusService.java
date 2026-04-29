package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobStatusService {
    private final JobRepository jobRepository;

    public void process(Job job, Runnable task) {
        job.setStatus(JobStatus.PROCESSING);
        job.setAttempts(job.getAttempts() + 1);
        jobRepository.save(job);

        try {
            task.run();
            job.setStatus(JobStatus.DONE);
        } catch (Exception e) {
            if (job.getAttempts() >= 5) {
                job.setStatus(JobStatus.FAILED);
            } else {
                // This is a retry mechanism
                job.setStatus(JobStatus.PENDING);
            }
        } finally {
            jobRepository.save(job);
        }
    }
}
