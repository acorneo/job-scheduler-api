package me.acorneo.jobschedulerapi.service.workers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.dto.jobs.CpuPayload;
import org.springframework.stereotype.Service;

@Service
public class CpuWorkerService {
    private int fib(int n) {
        if (n <= 1) return n;
        return fib(n-1) + fib(n-2);
    }

    public void handleCpu(Job job) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CpuPayload payload = mapper.readValue(job.getPayload(), CpuPayload.class);

        fib(payload.getN());
    }
}
