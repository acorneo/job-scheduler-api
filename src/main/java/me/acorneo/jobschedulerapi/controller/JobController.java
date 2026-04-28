package me.acorneo.jobschedulerapi.controller;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.CreateJobRequest;
import me.acorneo.jobschedulerapi.dto.CreateJobResponse;
import me.acorneo.jobschedulerapi.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/job")
public class JobController {
    private final JobService jobService;

    @PostMapping("/")
    public ResponseEntity<CreateJobResponse> createJob(@RequestBody CreateJobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                jobService.createJob(request)
        );
    }
}
