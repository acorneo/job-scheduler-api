package me.acorneo.jobschedulerapi.repository;

import me.acorneo.jobschedulerapi.entity.Job;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobRepository extends CrudRepository<Job, String> {
    List<Job> findByStatus(JobStatus status);
}
