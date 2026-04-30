package me.acorneo.jobschedulerapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import me.acorneo.jobschedulerapi.service.JobQueueService;
import me.acorneo.jobschedulerapi.service.workers.CpuWorkerService;
import me.acorneo.jobschedulerapi.service.workers.FailWorkerService;
import me.acorneo.jobschedulerapi.service.workers.WaitWorkerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor
public class TaskProcessor implements Runnable {
    private final JobQueueService jobQueueService;
    private final JobRepository jobRepository;

    private final WaitWorkerService waitWorkerService;
    private final CpuWorkerService cpuWorkerService;
    private final FailWorkerService failWorkerService;

    private void handleRetry(Job job) {
        job = jobRepository.findById(job.getId()).orElse(job);

        if (job.getAttempts() >= 3) {
            jobQueueService.fail(job.getId());
        } else {
            jobQueueService.retry(job.getId());
        }
    }

    private void markAsDone(Job job) {
        jobQueueService.complete(job.getId());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Optional<Job> job = Optional.empty();
                boolean foundJob = false;
                while (!foundJob) {
                    job = jobQueueService.findOneJob(Thread.currentThread().getName());

                    if (job.isEmpty()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        foundJob = true;
                    }
                }

                processSingleJob(job.get());
            } catch (Exception e) {
                log.error("Critical error in worker loop, but I will survive!", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    log.info("Worker {} stopping via interrupt", Thread.currentThread().getName());
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processSingleJob(Job job) {
        log.info("Started working on job with type: {}", job.getType().toString());

        switch (job.getType()) {
            case WAIT -> {
                try {
                    waitWorkerService.handleWait(job);
                    markAsDone(job);
                } catch (Exception e) {
                    handleRetry(job);
                }
            }
            case FAILING -> {
                try {
                    failWorkerService.handleFail(job);
                    markAsDone(job);
                } catch (Exception e ) {
                    handleRetry(job);
                }
            }
            case CPU_TASK -> {
                try {
                    cpuWorkerService.handleCpu(job);
                    markAsDone(job);
                } catch (Exception e) {
                    handleRetry(job);
                }
            }
        }
    }
}
