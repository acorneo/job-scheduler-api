package me.acorneo.jobschedulerapi.service.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.dto.jobs.FailPayload;
import org.springframework.stereotype.Service;

@Service
public class FailWorkerService {
    public void handleFail(Job job) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FailPayload payload = mapper.readValue(job.getPayload(), FailPayload.class);

        if (Math.random() < payload.getProbability()) {
            throw new RuntimeException("Some fail happened!");
        }
    }
}
