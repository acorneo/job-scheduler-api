package me.acorneo.jobschedulerapi.configuration;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.CreateJobRequest;
import me.acorneo.jobschedulerapi.enums.JobType;
import me.acorneo.jobschedulerapi.service.JobService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// This is a spammer that creates jobs for the sake of fun
@Component
@RequiredArgsConstructor
public class JobSpamGenerator {
    private final JobService jobService;

    @EventListener(ApplicationReadyEvent.class)
    public void generate() {
        int totalJobs = 200;

        int waitJobs = (int) (0.2 * totalJobs);
        int cpuJobs = (int) (0.4 * totalJobs);
        int failJobs = (int) (0.4 * totalJobs);

        for (int i = 0; i < waitJobs; i++) {
            CreateJobRequest request = new CreateJobRequest();
            request.setType(JobType.WAIT);
            String payload = "{\"duration\": 3.0}";
            request.setPayload(payload);
            jobService.createJob(request);
        }

        for (int i = 0; i < cpuJobs; i++) {
            CreateJobRequest request = new CreateJobRequest();
            request.setType(JobType.CPU_TASK);
            String payload = "{\"n\": 10}"; // there was 50 but my macbook died so revert to 10 lol
            request.setPayload(payload);
            jobService.createJob(request);
        }

        for (int i = 0; i < failJobs; i++) {
            CreateJobRequest request = new CreateJobRequest();
            request.setType(JobType.FAILING);
            String payload = "{\"probability\": 0.9}";
            request.setPayload(payload);
            jobService.createJob(request);
        }
    }
}
