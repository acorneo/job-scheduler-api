package me.acorneo.jobschedulerapi.service;

import lombok.extern.slf4j.Slf4j;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import me.acorneo.jobschedulerapi.workers.TaskProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JobManagerService {
    private final JobRepository jobRepository;
    private final JobStatusService jobStatusService;
    private final ApplicationContext applicationContext;
    @Qualifier("workerExecutor")
    private final AsyncTaskExecutor asyncTaskExecutor;

    // No lombok because it seems that it doesn't work with @Qualifier, it doesn't copy it into the constructor
    public JobManagerService(JobRepository jobRepository, JobStatusService jobStatusService, ApplicationContext applicationContext, @Qualifier("workerExecutor") AsyncTaskExecutor asyncTaskExecutor) {
        this.jobRepository = jobRepository;
        this.jobStatusService = jobStatusService;
        this.applicationContext = applicationContext;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Scheduled(fixedRate = 5000)
    public void processJobs() {
        List<Job> jobList = jobRepository.findOldestPendingJobsNative(JobStatus.PENDING.toString());

        for (Job job : jobList) {
            asyncTaskExecutor.execute(() -> {
                try {
                    TaskProcessor task = applicationContext.getBean(TaskProcessor.class, job);
                    jobStatusService.process(job, task);
                } catch (Exception e) {
                    log.error("Failed to create TaskProcessor for job {}", job.getId(), e);
                }
            });
        }
    }
}
