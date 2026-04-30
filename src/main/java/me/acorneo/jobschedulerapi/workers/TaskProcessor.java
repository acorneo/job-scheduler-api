package me.acorneo.jobschedulerapi.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor
public class TaskProcessor implements Runnable {
    private final JobRepository jobRepository;

    private void handleRetry(Job job) {
        job = jobRepository.findById(job.getId()).orElse(job);

        if (job.getAttempts() >= 3) {
            job.setStatus(JobStatus.FAILED);
        } else {
            job.setStatus(JobStatus.PENDING);
        }
        jobRepository.save(job);
    }

    private void markAsDone(Job job) {
        job.setStatus(JobStatus.DONE);
        jobRepository.save(job);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Optional<Job> job = Optional.empty();
                boolean foundJob = false;
                while (!foundJob) {
                    job = jobRepository.findOneJob(Thread.currentThread().getName());

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
                    handleWait(job);
                    markAsDone(job);
                } catch (Exception e) {
                    handleRetry(job);
                }
            }
            case FAILING -> {
                try {
                    handleFail(job);
                    markAsDone(job);
                } catch (Exception e ) {
                    handleRetry(job);
                }
            }
            case CPU_TASK -> {
                try {
                    handleCpu(job);
                    markAsDone(job);
                } catch (Exception e) {
                    handleRetry(job);
                }
            }
        }
    }

    private void handleWait(Job job) throws JsonProcessingException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        WaitPayload payload = mapper.readValue(job.getPayload(), WaitPayload.class);

        Thread.sleep((long) (payload.getDuration() * 1000));
    }

    private void handleFail(Job job) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FailPayload payload = mapper.readValue(job.getPayload(), FailPayload.class);

        if (Math.random() < payload.getProbability()) {
            throw new RuntimeException("Some fail happened!");
        }
    }

    int fib(int n) {
        if (n <= 1) return n;
        return fib(n-1) + fib(n-2);
    }

    private void handleCpu(Job job) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CpuPayload payload = mapper.readValue(job.getPayload(), CpuPayload.class);

        fib(payload.getN());
    }
}
