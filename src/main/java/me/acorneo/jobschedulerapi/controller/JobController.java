package me.acorneo.jobschedulerapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.CreateJobRequest;
import me.acorneo.jobschedulerapi.dto.JobDto;
import me.acorneo.jobschedulerapi.service.JobCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/job")
public class JobController {
    private final JobCreationService jobCreationService;

    @PostMapping({"", "/"})
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody CreateJobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                jobCreationService.createJob(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                jobCreationService.getJob(id)
        );
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<JobDto>> getAllJobsWithPagination(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(
                jobCreationService.getAllJobs(page, size)
        );
    }
}
