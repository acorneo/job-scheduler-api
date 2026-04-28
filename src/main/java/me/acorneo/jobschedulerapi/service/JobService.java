package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.CreateJobRequest;
import me.acorneo.jobschedulerapi.dto.CreateJobResponse;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public CreateJobResponse createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setType(request.getType());
        job.setCreatedAt(LocalDateTime.now());
        job.setAttempts(0);
        job.setStatus(JobStatus.PENDING);
        job.setPayload(request.getPayload());

        Job saved = jobRepository.save(job);
        return CreateJobResponse.builder()
                .id(saved.getId())
                .type(saved.getType())
                .attempts(saved.getAttempts())
                .createdAt(saved.getCreatedAt())
                .payload(saved.getPayload())
                .status(saved.getStatus())
                .build();
    }
}
