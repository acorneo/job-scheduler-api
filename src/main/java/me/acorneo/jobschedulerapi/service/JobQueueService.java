package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobQueueService {
    private static final String PENDING_QUEUE = "jobs:queue";
    private static final String PROCESSING_QUEUE = "jobs:processing";

    private final JobRepository jobRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void enqueue(Job job) {
        job.setStatus(JobStatus.PENDING);
        Job saved = jobRepository.save(job);
        redisTemplate.opsForList().leftPush(PENDING_QUEUE, saved.getId());
    }

    public Optional<Job> findOneJob(String worker) {
        // RPOPLPUSH — atomic operation, workers won't receive the same job
        String jobId = redisTemplate.opsForList()
                .rightPopAndLeftPush(PENDING_QUEUE, PROCESSING_QUEUE);

        if (jobId == null) return Optional.empty();

        return jobRepository.findById(jobId).map(job -> {
            job.setStatus(JobStatus.PROCESSING);
            job.setWorker(worker);
            job.setAttempts(job.getAttempts() + 1);
            return jobRepository.save(job);
        });
    }

    public void complete(String jobId) {
        redisTemplate.opsForList().remove(PROCESSING_QUEUE, 1, jobId);
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(JobStatus.DONE);
            jobRepository.save(job);
        });
    }

    public void fail(String jobId) {
        redisTemplate.opsForList().remove(PROCESSING_QUEUE, 1, jobId);
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
        });
    }

    public void retry(String jobId) {
        redisTemplate.opsForList().remove(PROCESSING_QUEUE, 1, jobId);
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(JobStatus.PENDING);
            jobRepository.save(job);
            redisTemplate.opsForList().leftPush(PENDING_QUEUE, jobId);
        });
    }
}
