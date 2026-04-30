package me.acorneo.jobschedulerapi.service.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.dto.jobs.WaitPayload;
import org.springframework.stereotype.Service;

@Service
public class WaitWorkerService {
    public void handleWait(Job job) throws JsonProcessingException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        WaitPayload payload = mapper.readValue(job.getPayload(), WaitPayload.class);

        Thread.sleep((long) (payload.getDuration() * 1000));
    }
}
