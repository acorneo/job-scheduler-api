package me.acorneo.jobschedulerapi.workers;

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
        log.info("Payload: {}", job.getPayload());
    }
}
