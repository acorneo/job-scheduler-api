package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.CreateJobRequest;
import me.acorneo.jobschedulerapi.dto.JobDto;
import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.exception.NotFoundException;
import me.acorneo.jobschedulerapi.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public JobDto createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setType(request.getType());
        job.setCreatedAt(LocalDateTime.now());
        job.setAttempts(0);
        job.setStatus(JobStatus.PENDING);
        job.setPayload(request.getPayload());

        Job saved = jobRepository.save(job);
        return JobDto.builder()
                .id(saved.getId())
                .type(saved.getType())
                .attempts(saved.getAttempts())
                .createdAt(saved.getCreatedAt())
                .payload(saved.getPayload())
                .status(saved.getStatus())
                .build();
    }

    public JobDto getJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isEmpty()) {
            throw new NotFoundException("Job with this ID was not found.");
        }

        Job found = job.get();

        return JobDto.builder()
                .id(found.getId())
                .status(found.getStatus())
                .type(found.getType())
                .payload(found.getPayload())
                .attempts(found.getAttempts())
                .createdAt(found.getCreatedAt())
                .build();
    }

    public List<JobDto> getAllJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobList = jobRepository.findAll(pageable);

        return jobList.getContent()
                .stream()
                .map(job -> JobDto
                        .builder()
                        .id(job.getId())
                        .status(job.getStatus())
                        .type(job.getType())
                        .payload(job.getPayload())
                        .attempts(job.getAttempts())
                        .createdAt(job.getCreatedAt())
                        .build())
                .toList();
    }
}
