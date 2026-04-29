package me.acorneo.jobschedulerapi.configuration;

import lombok.RequiredArgsConstructor;
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
        // todo
    }
}
