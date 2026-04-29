package me.acorneo.jobschedulerapi.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.acorneo.jobschedulerapi.entity.Job;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor
public class TaskProcessor implements Runnable {
    private Job job;

    public TaskProcessor(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        log.info("Started working on job with type: {}", job.getType().toString());

        switch (job.getType()) {
            case WAIT -> {
                try {
                    handleWait();
                } catch (JsonProcessingException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            case FAILING -> {
                try {
                    handleFail();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            case CPU_TASK -> {
                try {
                    handleCpu();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleWait() throws JsonProcessingException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        WaitPayload payload = mapper.readValue(job.getPayload(), WaitPayload.class);

        Thread.sleep((long) (payload.getDuration() * 1000));
    }

    private void handleFail() throws JsonProcessingException {
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

    private void handleCpu() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CpuPayload payload = mapper.readValue(job.getPayload(), CpuPayload.class);

        int result = fib(payload.getN());
    }
}
